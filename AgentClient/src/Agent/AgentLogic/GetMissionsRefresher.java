package Agent.AgentLogic;

import DataTransferObject.AgentMissionDTO;
import DataTransferObject.UboatDTO;
import com.sun.istack.internal.NotNull;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static Agent.AgentUI.main.utils.Constants.GET_AGENT_MISSIONS_LIST;
import static utils.Constants.GSON_INSTANCE;

public class GetMissionsRefresher extends TimerTask {
    private final Consumer<List<AgentMissionDTO>> missionsConsumer;
    private BooleanProperty shouldUpdate;
    private AgentMissionCounter agentMissionCounter;

    public GetMissionsRefresher(Consumer<List<AgentMissionDTO>> missionsConsumer, BooleanProperty shouldUpdate,
                                AgentMissionCounter agentMissionCounter) {
        this.missionsConsumer = missionsConsumer;
        this.shouldUpdate = shouldUpdate;
        this.agentMissionCounter = agentMissionCounter;
    }

    @Override
    public void run() {
        if (agentMissionCounter.getMissionsOnQueue() > 0) {
            return;
        }
        String finalUrl = HttpUrl
                .parse(GET_AGENT_MISSIONS_LIST)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Agent: Get missions refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    String jsonMissionList = response.body().string();
                    AgentMissionDTO[] agentMissionArray = GSON_INSTANCE.fromJson(jsonMissionList, AgentMissionDTO[].class);
                    List<AgentMissionDTO> agentMissionList = Arrays.asList(agentMissionArray);
                    response.body().close();
                    if (!agentMissionList.isEmpty()) {
                        missionsConsumer.accept(agentMissionList);
                    }
                }
            }
        });
    }
}