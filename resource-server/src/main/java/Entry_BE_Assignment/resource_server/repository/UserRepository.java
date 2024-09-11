package Entry_BE_Assignment.resource_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Entry_BE_Assignment.resource_server.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
