package spring.labserver.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

// User 레포지토리 <Entity 클래스, PK 타입>
public interface UserRepository extends JpaRepository<User, String> {
    public User findByName(String name);
}
