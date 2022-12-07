package Agent.AgentLogic;

import DataTransferObject.ContestStatusDTO;
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

import static Agent.AgentUI.main.utils.Constants.GET_CONTEST_STATUS_SERVLET;
import static utils.Constants.GSON_INSTANCE;

public class CheckGameStatusRefresher extends TimerTask {
    private final Consumer<ContestStatusDTO> contestStatusConsumer;
    private BooleanProperty shouldUpdate;

    public CheckGameStatusRefresher(Consumer<ContestStatusDTO> contestStatusConsumer, BooleanProperty shouldUpdate) {
        this.contestStatusConsumer = contestStatusConsumer;
        this.shouldUpdate = shouldUpdate;
    }

    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }
        String finalUrl = HttpUrl
                .parse(GET_CONTEST_STATUS_SERVLET)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Agent: Check game status refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    String jsonContestStatus = response.body().string();
                    ContestStatusDTO contestStatusDTO = GSON_INSTANCE.fromJson(jsonContestStatus, ContestStatusDTO.class);
                    response.body().close();
                    contestStatusConsumer.accept(contestStatusDTO);
                }else{
                    System.out.println("Something went wrong!!!");
                }
            }
        });
    }
}