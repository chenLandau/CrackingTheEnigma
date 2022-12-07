package Allies.component.ContestScreen.SelectedContestDataArea;

import Allies.main.util.Constants;

import DataTransferObject.UboatDTO;
import com.sun.istack.internal.NotNull;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static utils.Constants.*;
public class SelectedContestDataRefresher extends TimerTask {
    private final Consumer<UboatDTO> uboatDtoConsumer;
    private final BooleanProperty shouldUpdate;

    public SelectedContestDataRefresher(Consumer<UboatDTO> uboatListConsumer, BooleanProperty shouldUpdate) {
        this.uboatDtoConsumer = uboatListConsumer;
        this.shouldUpdate = shouldUpdate;
    }

    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }

        String finalUrl = HttpUrl
                .parse(GET_SELECTED_CONTEST_DATA)
                .newBuilder()
                .addQueryParameter("userType", "Allies")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Ally: Get selected contest data refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                        String jsonUboatDTO = response.body().string();
                        UboatDTO uboatDTO = GSON_INSTANCE.fromJson(jsonUboatDTO, UboatDTO.class);
                        uboatDtoConsumer.accept(uboatDTO);
                }
            }
        });
    }
}
