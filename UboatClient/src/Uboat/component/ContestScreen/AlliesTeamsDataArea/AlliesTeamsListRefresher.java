package Uboat.component.ContestScreen.AlliesTeamsDataArea;

import DataTransferObject.AllyTeamDTO;
import com.sun.istack.internal.NotNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static Uboat.main.utils.Constants.GET_ALLIES_TEAMS_LIST;
import static utils.Constants.*;


public class AlliesTeamsListRefresher extends TimerTask {
    private final Consumer<List<AllyTeamDTO>> AlliesTeamsListConsumer;

    public AlliesTeamsListRefresher(Consumer<List<AllyTeamDTO>> AlliesTeamsListConsumer) {
        this.AlliesTeamsListConsumer = AlliesTeamsListConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(GET_ALLIES_TEAMS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
               // httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    String jsonAlliesTeamsList = response.body().string();
                    AllyTeamDTO[] alliesTeamsList = GSON_INSTANCE.fromJson(jsonAlliesTeamsList, AllyTeamDTO[].class);
                    response.body().close();
                    if(alliesTeamsList != null && alliesTeamsList.length > 0)
                        AlliesTeamsListConsumer.accept(Arrays.asList(alliesTeamsList));
                }
            }
        });
    }
}
