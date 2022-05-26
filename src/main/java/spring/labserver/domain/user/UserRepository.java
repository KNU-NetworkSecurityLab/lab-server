package spring.labserver.domain.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import spring.labserver.domain.user.query.UserInfoInterface;

// User 레포지토리 <Entity 클래스, PK 타입>
public interface UserRepository extends JpaRepository<User, Long> {
    // // findBy~~ 로 시작해서 엔티티로부터 쿼리를 요청하는 메소드 생성
    public User findByUserId(String userId);
    public boolean existsByUserId(String userId);

    // 일반 JPQL쿼리, Interface를 이용해서 특정 컬럼만 조회하는 쿼리문 작성
    @Query(value = "SELECT u FROM User u WHERE u.role = 'ROLE_USER'")
    public List<UserInfoInterface> findAllUserInfoByRole();
}
