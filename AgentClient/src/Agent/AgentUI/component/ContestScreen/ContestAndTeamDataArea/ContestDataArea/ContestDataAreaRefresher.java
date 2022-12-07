package Agent.AgentUI.component.ContestScreen.ContestAndTeamDataArea.ContestDataArea;

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

import static Agent.AgentUI.main.utils.Constants.GET_CONTEST_LIST_SERVLET_FOR_AGENT;
import static utils.Constants.*;

public class ContestDataAreaRefresher extends TimerTask {
    private final Consumer<UboatDTO> contestDataConsumer;
    private BooleanProperty shouldUpdate;

    public ContestDataAreaRefresher(Consumer<UboatDTO> contestDataConsumer, BooleanProperty shouldUpdate) {
        this.contestDataConsumer = contestDataConsumer;
        this.shouldUpdate = shouldUpdate;
    }


    @Override
    public void run()  {
        if (!shouldUpdate.get()) {
            return;
        }
        String finalUrl = HttpUrl
                .parse(GET_CONTEST_LIST_SERVLET_FOR_AGENT)
                .newBuilder()
                .addQueryParameter("userType", "Agent")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Agent: get contest data refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    String jsonUboatDTO = response.body().string();
                    UboatDTO uboatDTO = GSON_INSTANCE.fromJson(jsonUboatDTO, UboatDTO.class);
                    if(uboatDTO != null) {
                        contestDataConsumer.accept(uboatDTO);
                    }
                }
            }
        });
    }
}
