package me.mutashim.votesmart.repository;
import me.mutashim.votesmart.model.Poll;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PollRepository extends MongoRepository<Poll, String> {
    List<Poll> findByTitleContaining(String title);  // Example custom method to search polls by title
    List<Poll> findByCreatorId(String creatorId);
}
