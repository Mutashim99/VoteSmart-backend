package me.mutashim.votesmart.repository;

import me.mutashim.votesmart.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface VoteRepository extends MongoRepository<Vote, String> {
    List<Vote> findByPollId(String pollId);
    List<Vote> findByUserId(String userId);
    boolean existsByPollIdAndUserId(String pollId, String userId);
}
