# zerobase-Misson1-3

구현 기능

로그인 3종류- 사용자, 점장, 키오스크 (키오스크는 매장 등록시 아이디 부여, 점장도 다른가게 예약을 위해서는 사용자로 가입해야함)

사용자
-회원 가입
-매장검색/상세 정보 보기
-방문예약 등록
-내 예약 조회
-리뷰 작성 (방문이 완료된 예약에 한해서만)


점장
-파트너 가입
-매장 등록(파트너 가입 필요,매장 등록시 키오스크 자동 등록:매장번호와 점장의 비밀번호)
-신청된 예약 조회
-예약 승인/거절

키오스크 
-방문 확인(예약 10분전부터 예약승인 된것만 가능,예약시간보다 10분 이후에 오면 방문확인 불가, 본인확인은 이름과 핸드폰번호로)
----------------------------
환경
JDK 17
스프링 부트
그래들
mysql
jpa
----------------------------
사용 기술
lombok
jpa
spring-security
----------------------------
테이블 -USER, STORE, RESERVATION


USER -사용자 정보 테이블
-ID VARCHAR(사용자 아이디)
-PW VARCHAR (암호화 저장)
-ROLE ENUM (사용자, 점장, 키오스크. 사용자는 상점 등록 불가, 점장도 다른매장 이용/검색을 위해서는 사용자계정도 가입해야함)
-NAME VARCHAR (사용자 이름)
-PHONE INT (연락처)
-EMAIL VARCHAR(이메일 주소)
-REG_DT DATETIME (등록일)
-UP_DT DATETIME (수정일)



STORE - 상점 정보 테이블
-STORE_NO INT PK (조회를 위한 상점 번호)
-USER_ID STRING (상점 주인의 유저번호)
-NAME VARCHAR(50) NOT NULL(상점명)
-ADDRESS1 VARCHAR(30) NOT NULL(도로명 주소)
-ADDRESS2 VARCHAR(30) NOT NULL(상세 주소)
-DESCRIPTION VARCHAR(50) NOT NULL(상점 설명)
-REG_DT DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP (등록일)
-UP_DT DATETIME ON UPDATE CURRENT_TIMESTAMP(수정일)

RESERVATION -예약 정보 테이블 -예약 하나당 테이블 1줄 차지. 예약 변경 불가, 취소후 재예약은 가능 (데이터 무결성 위반 방지)
-RESERV_NO INT PK(조회를 위한 예약 번호)
-USER_NO LONG (등록자 아이디)
-STORE_NO LONG  (어떤 상점의 예약 정보인지 알기위한 상점 번호)
-RESERV_DT DATETIME  (예약시간)
-REG_DT DATETIME  (등록일)
-MESSAGE VARCHAR (요청 사항- 제로 콜라로 바꿔주세요 등)
-APPROVED BIT (승인 여부. 해당 상점 점장이 승인 가능)
-VISITED BIT (방문확인 여부. 예약시간 10분전부터 예약시간 10분후까지만 가능)
-REVIEW VARCHAR (리뷰. 방문확인 처리후에만 작성 가능)
--------------------------------------------------
컨트롤러, 서비스 설계
컨트롤러는 권한 단위로 묶음
CustommerController
KioskController
MainController  (로그인하기전 페이지인 로그인,회원가입 등을 담당)
ManagerController 

뷰도 권한 단위로 묶음
templates.common
templates.customer
templates.kiosk
templates.manager


서비스는 기능(또는 테이블) 단위로 묶음
ReservService
StoreService
UserService
