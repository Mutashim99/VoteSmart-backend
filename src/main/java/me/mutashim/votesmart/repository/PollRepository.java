package me.mutashim.votesmart.repository;
import me.mutashim.votesmart.model.Poll;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PollRepository extends MongoRepository<Poll, String> {
    List<Poll> findByTitleContaining(String title);
    List<Poll> findByCreatorId(String creatorId);
}
