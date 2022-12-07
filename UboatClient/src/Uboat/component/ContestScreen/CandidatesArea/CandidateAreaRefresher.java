package Uboat.component.ContestScreen.CandidatesArea;

import DataTransferObject.CandidatesAndVersionForUboatDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static Uboat.main.utils.Constants.GET_NEW_CANDIDATES;
import static utils.Constants.GSON_INSTANCE;

public class CandidateAreaRefresher extends TimerTask {
    private final Consumer<CandidatesAndVersionForUboatDTO> candidatesAndVersionConsumer;
    private final IntegerProperty candidateVersion;
    private final BooleanProperty shouldUpdate;
    private int requestNumber;

    public CandidateAreaRefresher(IntegerProperty candidateVersion, BooleanProperty shouldUpdate, Consumer<CandidatesAndVersionForUboatDTO> candidatesAndVersionConsumer) {
        this.candidatesAndVersionConsumer = candidatesAndVersionConsumer;
        this.candidateVersion = candidateVersion;
        this.shouldUpdate = shouldUpdate;
        requestNumber = 0;
    }

    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }

        String finalUrl = HttpUrl
                .parse(GET_NEW_CANDIDATES)
                .newBuilder()
                .addQueryParameter("chatversion", String.valueOf(candidateVersion.get()))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Uboat: Get new candidates refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String rawBody = response.body().string();
                    CandidatesAndVersionForUboatDTO candidatesAndVersionDTO = GSON_INSTANCE.fromJson(rawBody, CandidatesAndVersionForUboatDTO.class);
                    candidatesAndVersionConsumer.accept(candidatesAndVersionDTO);
                }
            }
        });

    }

}
