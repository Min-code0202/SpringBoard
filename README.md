# CRUD 게시판 프로젝트  

## 프로젝트 소개  
Spring Boot를 사용하여 구현한 CRUD 게시판 프로젝트입니다.  
**파일(이미지) 첨부** 및 **페이징 처리**와 같은 실무에 유용한 기능들을 포함하고 있으며, **Thymeleaf**를 활용한 템플릿 기반 UI로 구성되었습니다.  

---

## 개발 환경  
- **IDE**: IntelliJ IDEA  
- **Spring Boot**: 3.4.0  
- **JDK**: 17  
- **Database**: MySQL  
- **ORM**: Spring Data JPA  
- **Frontend**: Thymeleaf  

---

## 주요 기능  
### 1. 게시판 CRUD  
- **글쓰기**: `/board/save`  
- **글목록 보기**: `/board/`  
- **글 조회**: `/board/{id}`  
- **글 수정**:  
  - 상세 화면에서 수정 버튼 클릭  
  - 서버에서 게시글 정보를 수정 화면에 출력  
  - 제목 및 내용을 수정 후 요청  
  - 수정 처리 완료  
  - URL: `/board/update/{id}`  
- **글 삭제**: `/board/delete/{id}`  

### 2. 페이징 처리  
- URL 예시:  
  - `/board/paging?page=2`  
  - `/board/paging/2`  
- **페이징 동작 예시**:  
  - 게시글 14개  
  - 한 페이지에 5개씩 표시 → 총 3페이지  
  - 한 페이지에 3개씩 표시 → 총 5페이지  

### 3. 파일 첨부  
- 단일 파일(이미지) 첨부 기능 지원  