package backend.user.service;

import backend.common.model.Vote;
import backend.entry.model.Entry;
import backend.entry.model.EntryType;
import backend.entry.service.EntryService;
import backend.user.model.ActivityInfo;
import backend.user.model.VotesStatistics;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Service
public class ActivityStatisticsService {

    private final EntryService entryService;

    public ActivityStatisticsService(EntryService entryService) {
        this.entryService = entryService;
    }

    public ActivityInfo getUserActivityInfo(Integer userId) {

        ActivityInfo.ActivityInfoBuilder activityInfoBuilder = ActivityInfo.builder();
        activityInfoBuilder.noEntries(getUserNoEntries(userId));
        activityInfoBuilder.noVotes(getUserVotesStatistics(userId));

        return activityInfoBuilder.build();
    }

    public Map<String, Long> getUserNoEntries(Integer userId) {
        List<Entry> userEntries = entryService.getEntries(null, null, userId, null, List.of());
        Map<String, Long> noEntries =  userEntries.stream().collect(Collectors.groupingBy(
                (entry) -> entry.getType().getName(), Collectors.counting()
        ));
        noEntries.putIfAbsent(EntryType.POST, 0L);
        noEntries.putIfAbsent(EntryType.ANNOUNCEMENT, 0L);
        noEntries.putIfAbsent(EntryType.NOTE, 0L);
        return noEntries;
    }

    public Map<String, VotesStatistics> getUserVotesStatistics(Integer userId) {
        List<Entry> userEntries = entryService.getEntries(null, null, userId, null, List.of());
        Map<String, List<Entry>> groupedEntries = userEntries.stream().collect(Collectors.groupingBy(
                (entry) -> entry.getType().getName()
        ));

        Map<String, VotesStatistics> noVotes = new HashMap<>();
        groupedEntries.forEach((entryType, entries) -> {
            Map<Object, Long> cummulativeVotes =
                    entries.stream().flatMap((entry) ->
                         entry.getVotes().stream().collect(Collectors.groupingBy(Vote::getValue, Collectors.counting())).
                             entrySet().stream()).collect(Collectors.groupingBy(Map.Entry::getKey,Collectors.summingLong(Map.Entry::getValue)));
            VotesStatistics.VotesStatisticsBuilder statisticsBuilder = VotesStatistics.builder();
            statisticsBuilder.positive(cummulativeVotes.getOrDefault(1, 0L));
            statisticsBuilder.negative(cummulativeVotes.getOrDefault(-1, 0L));
            noVotes.put(entryType, statisticsBuilder.build());
        });
        noVotes.putIfAbsent(EntryType.POST, VotesStatistics.builder().negative(0L).positive(0L).build());
        noVotes.putIfAbsent(EntryType.ANNOUNCEMENT, VotesStatistics.builder().negative(0L).positive(0L).build());
        noVotes.putIfAbsent(EntryType.NOTE, VotesStatistics.builder().negative(0L).positive(0L).build());
        return noVotes;
    }

}
