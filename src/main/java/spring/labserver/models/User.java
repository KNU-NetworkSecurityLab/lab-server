package spring.labserver.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Getter 메소드 추가
@Getter
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
    private String id;    

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String name;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String password;

    @Column(columnDefinition = "VARCHAR(50)",nullable = false)
    private String mail;

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    private String phone;

    // USER, ADMIN, GUEST
    private String role;
    
    // 역할 반환
    public List<String> getRoleList() {
        if(this.role.length() > 0) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }

    @Builder
    public User(String id, String name, String password, String mail, String phone, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.phone = phone;
        this.role = role;
    }

}
// 아래 데이터베이스 명세서 참고
// https://www.notion.so/BE-ad136c7a76a54ff2bf547921dbf02baf#dc759c716c224aaab74c542b1e29ad12
