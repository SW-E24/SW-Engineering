# recipe-demo
[재료백과 웹 사이트 실행 방법]

1. 프로젝트 파일 설치 및 실행
- 첨부된 프로젝트 파일을 설치합니다.
- SW-Engineering\recipe\recipe 프로젝트 폴더를 인텔리제이로 오픈합니다.
- RecipeApplication 을 실행합니다. (java/com/example/recipe/RecipeApplication.java)

* 사용중인 포트 문제로 인해 실행이 되지 않을 경우
- 재료백과 프로젝트는 8080 을 기본으로 실행합니다.
- 다른 서버 포트로 변경이 필요할 시 application.properties 파일 최상단에 server.port = [원하는 포트 번호] 를 추가해서 실행합니다.

2. 웹 사이트 접속
- 웹 브라우저에서 localhost:8080 으로 접속합니다.
(포트 번호를 변경하였다면 그에 맞는 번호를 입력하여 접속합니다. localhost:{변경된번호})

3. 재료백과 기능 테스트
- 모든 기능을 테스트하기 위해선 '회원가입'이 필수입니다.
- 우측 상단 메뉴의 회원가입 버튼을 눌러 회원가입을 진행해주세요.
- 회원가입을 진행한 사용자 정보로 다시 로그인을 진행한 뒤, 재료백과의 모든 기능을 사용할 수 있습니다.

<주요 기능 실행 방법>
- 메뉴 추천, 카테고리 분류, 검색, 게시글 및 댓글 작성/수정/삭제, 마이페이지 (북마크, 좋아요, 작성한 글/댓글, 등급 확인), 회원정보 수정
- 자세한 실행 과정은 같이 첨부된 SDS 문서의 use case 를 참고해주세요.

4. 데이터베이스 레코드 저장 내용 확인
- localhost:8080/h2-console 에 접속하여 h2 콘솔을 실행합니다.
- 포트 번호를 변경하였을 경우 그에 맞는 번호로 수정해주세요. localhost:{변경된번호}/h2-console

- h2 콘솔 로그인을 위해 아래와 같이 설정해줍니다.
    - Saved Settings : Generic H2 (Embedded)
    - Setting Name : Generic H2 (Embedded)
    - Driver Class : org.h2.Driver
    - JDBC URL : jdbc:h2:~/local/recipe
    - User Name : sa
    - Password : (공백으로 비워주세요)
- Test Connection 버튼을 눌러 'Test successful'이 뜨면 Connect 버튼을 눌러 데이터베이스에 진입할 수 있습니다.

- 사용하는 테이블은 총 9개 입니다. (BOOKMARK, COMMENT, GRADE, LIKES, RECIPE, RECIPE_INGREDIENTS, RECIPE_STEPS, USER_TABLE, VIEW)
- 적절한 쿼리문을 사용해 테이블에 저장된 레코드를 확인하실 수 있습니다.