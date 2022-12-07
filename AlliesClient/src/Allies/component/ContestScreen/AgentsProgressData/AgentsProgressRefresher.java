package Allies.component.ContestScreen.AgentsProgressData;

import DataTransferObject.AgentProgressDTO;
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

import static Allies.main.util.Constants.GET_AGENT_PROGRESS_LIST;
import static utils.Constants.GSON_INSTANCE;


public class AgentsProgressRefresher extends TimerTask {
    private final Consumer<List<AgentProgressDTO>> agentProgressListConsumer;

    public AgentsProgressRefresher(Consumer<List<AgentProgressDTO>> agentProgressListConsumer) {
        this.agentProgressListConsumer = agentProgressListConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsync(GET_AGENT_PROGRESS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on Ally: Get agents progress list refresher");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonAgentProgressList= response.body().string();
                AgentProgressDTO[] agentProgressList = GSON_INSTANCE.fromJson(jsonAgentProgressList, AgentProgressDTO[].class);
                agentProgressListConsumer.accept(Arrays.asList(agentProgressList));
            }
        });
    }
}
