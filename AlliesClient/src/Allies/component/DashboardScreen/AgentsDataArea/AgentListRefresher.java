package Allies.component.DashboardScreen.AgentsDataArea;

import Allies.main.util.Constants;
import DataTransferObject.AgentDTO;
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
import static utils.Constants.GSON_INSTANCE;

public class AgentListRefresher extends TimerTask {
    private final Consumer<List<AgentDTO>> agentListConsumer;
    public AgentListRefresher(Consumer<List<AgentDTO>> agentListConsumer) {
        this.agentListConsumer = agentListConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(Constants.AGENT_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Ally: Get agent list refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfAgents = response.body().string();
                AgentDTO[] agentList = GSON_INSTANCE.fromJson(jsonArrayOfAgents, AgentDTO[].class);
                agentListConsumer.accept(Arrays.asList(agentList));
            }
        });
    }
}
