package backend.user.service;

import backend.answer.model.Answer;
import backend.answer.service.AnswerService;
import backend.common.model.Vote;
import backend.common.service.GenericServiceException;
import backend.common.service.VoteService;
import backend.entry.model.Entry;
import backend.entry.model.EntryType;
import backend.entry.service.EntryService;
import backend.user.model.ActivityInfo;
import backend.user.model.User;
import backend.user.model.VotesStatistics;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Map<String, Integer> getUserNoEntries(Integer userId) {
        List<Entry> userEntries = entryService.getEntries(null, null, userId, null, List.of());
        Map<String, List<Entry>> groupedEntries = userEntries.stream().collect(Collectors.groupingBy(
                (entry) -> entry.getType().getName()
        ));
        Map<String, Integer> noEntries = new HashMap<>();
        noEntries.put(EntryType.POST, groupedEntries.getOrDefault(EntryType.POST, List.of()).size());
        noEntries.put(EntryType.NOTE, groupedEntries.getOrDefault(EntryType.NOTE, List.of()).size());
        noEntries.put(EntryType.ANNOUNCEMENT, groupedEntries.getOrDefault(EntryType.ANNOUNCEMENT, List.of()).size());
        return noEntries;
    }

    public Map<String, VotesStatistics> getUserVotesStatistics(Integer userId) {
        List<Entry> userEntries = entryService.getEntries(null, null, userId, null, List.of());
        Map<String, List<Entry>> groupedEntries = userEntries.stream().collect(Collectors.groupingBy(
                (entry) -> entry.getType().getName()
        ));

        Map<String, VotesStatistics> noVotes = new HashMap<>();
        for (String entryType : groupedEntries.keySet()) {
            List<Entry> entries = groupedEntries.get(entryType);
            VotesStatistics.VotesStatisticsBuilder statisticsBuilder = VotesStatistics.builder();
            Map<Integer, Integer> cummulativeVotes = new HashMap<>();
            cummulativeVotes.put(-1, 0);
            cummulativeVotes.put(1, 0);
            for (Entry entry: entries) {
                Map<Integer, List<Vote>> votes = entry.getVotes().stream().collect(Collectors.groupingBy(Vote::getValue));
                votes.keySet().stream().forEach((key) -> {
                    Integer sum = votes.get(key).stream().mapToInt(Vote::getValue).reduce(0, Integer::sum);
                    cummulativeVotes.computeIfPresent(key, (voteVal ,voteCount) -> voteCount + abs(sum));
                });
            }
            statisticsBuilder.positive(cummulativeVotes.get(1));
            statisticsBuilder.negative(cummulativeVotes.get(-1));
            noVotes.put(entryType, statisticsBuilder.build());
        }
        noVotes.putIfAbsent(EntryType.ANNOUNCEMENT, VotesStatistics.builder().positive(0).negative(0).build());
        noVotes.putIfAbsent(EntryType.NOTE, VotesStatistics.builder().positive(0).negative(0).build());
        noVotes.putIfAbsent(EntryType.POST, VotesStatistics.builder().positive(0).negative(0).build());
        return noVotes;
    }

}
