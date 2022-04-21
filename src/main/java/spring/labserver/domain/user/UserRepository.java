package spring.labserver.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

// User 레포지토리 <Entity 클래스, PK 타입>
public interface UserRepository extends JpaRepository<User, Long> {
    // findBy~~ 로 시작해서 엔티티로부터 쿼리를 요청하는 메소드 생성
    public User findByUserId(String userId);
}
