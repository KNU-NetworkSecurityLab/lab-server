package spring.labserver.domain.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Getter 메소드 추가
@Data
// 인자없는 기본 생성자 추가
@NoArgsConstructor
// 테이블과 링크될 엔티티 클래스임을 선언
@Entity
// 테이블 이름 명시
// 테이블 이름을 명시하지 않으면 CamelCase로 테이블이 생성 됨
// 테이블 이름을 짓는 기본 규칙은 UnderScore이기 때문에 이름을 명시하는게 좋음
@Table(name = "USER")
public class User {
    // PK키 설정
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    

    @Column(nullable = false, length = 20, unique = true)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String mail;

    @Column(nullable = false)
    private String phone;

    // USER,ADMIN,GUEST
    @Column(nullable = false)
    private String role;
    
    public void update(String password, String phone, String mail) {
        this.password = password;
        this.phone = phone;
        this.mail = mail;
    }
    
    @Builder
    public User(String userId, String name, String password, String mail, String phone, String role) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.phone = phone;
        this.role = role;
    }

    // 역할 반환
    // ENUM으로 안하고 ,로 구분해서 ROLE을 입력한뒤 그걸 파싱
    public List<String> getRoleList() {
        if(this.role.length() > 0) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }

}
// 아래 데이터베이스 명세서 참고
// https://www.notion.so/BE-ad136c7a76a54ff2bf547921dbf02baf#dc759c716c224aaab74c542b1e29ad12
