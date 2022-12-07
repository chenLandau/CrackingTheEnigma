package Allies.component.ContestScreen.AlliesTeamsDataArea;

import DataTransferObject.AllyTeamDTO;
import com.sun.istack.internal.NotNull;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static Allies.main.util.Constants.GET_ALLIES_TEAMS_LIST;
import static utils.Constants.GSON_INSTANCE;


public class AlliesTeamsListRefresher extends TimerTask {
    private final Consumer<List<AllyTeamDTO>> AlliesTeamsListConsumer;
    private final BooleanProperty shouldUpdate;

    public AlliesTeamsListRefresher(Consumer<List<AllyTeamDTO>> AlliesTeamsListConsumer, BooleanProperty shouldUpdate) {
        this.AlliesTeamsListConsumer = AlliesTeamsListConsumer;
        this.shouldUpdate = shouldUpdate;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(GET_ALLIES_TEAMS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Ally: Get allies teams list refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonAlliesTeamsList= response.body().string();
                AllyTeamDTO[] alliesTeamsList = GSON_INSTANCE.fromJson(jsonAlliesTeamsList, AllyTeamDTO[].class);
                AlliesTeamsListConsumer.accept(Arrays.asList(alliesTeamsList));
            }
        });
    }
}
