## Actify : 대학생들을 위한 액티비티 모임 앱

### 📌 서비스 목적 및 기획 의도

- **액티파이(Actify)** 
    - "Act(행동하다)" + "-ify(~하게 하다)"
    - "활동을 시작하게 하다", "활기차게 만들다"
- **대상**
  - 대학생
- **목표**
  - 책상에 오래 앉아 신체 활동이 부족한 대학생들에게 다양한 스포츠 및 액티비티 모임을 통해 신체·정신 건강 증진 및 새로운 교류 기회 제공
- **서비스 가치**
  - 스포츠·액티비티 모임 접근성 향상
  - 단순하고 효율적인 기능
  - 트렌디한 UI

<br>

### 📌 서비스 디자인

- **로고**
  - 달려가는 사람의 속도감을 형상화
- **컬러 팔레트**
  - 경기장의 잔디를 연상시키는 짙은 초록색 사용
- **디자인 방향**
  - 깔끔한 UI와 볼드한 디자인으로 역동성 강조
<img width="1594" alt="Image" src="https://github.com/user-attachments/assets/e90d2a33-472a-4729-83ab-cf1a85dde9f2" />

<br>

### 📌 데이터베이스 구조

- **사용자 관련 테이블**
    - tb_user: 사용자의 기본 정보(ID, 비밀번호, 이름, 생일, 전화번호, 대학교, 전공)
    - tb_leader: 클럽 리더의 정보와 소개

- **동호회 관련 테이블**
    - tb_club: 클럽의 기본 정보(이름, 짧은 소개, 날짜, 시간, 위치, 요구사항, 비용 등)
    - tb_club_details: 클럽의 상세 정보와 프로그램 내용
    - 
- **활동 관련 테이블**
    - tb_like: 사용자의 클럽 좋아요 정보
    - tb_application: 클럽 신청 정보
    - tb_review: 클럽에 대한 리뷰와 평점
    - tb_faq: 클럽 관련 질문과 답변
      
<img width="1643" alt="Image" src="https://github.com/user-attachments/assets/f534157f-d829-47d8-af62-fe0a939b2022" />

<br>

### 📌 외부 API

- **오픈웨더(OpenWeather)**
  - 날씨 정보 제공

<br>

### 📌 주요 기능

- **회원가입 및 로그인**
    - 아이디, 비밀번호, 개인정보 입력(중복 아이디, 빠진 정보 → 가입 불가)
    - 로그인 시 아이디·비밀번호 입력, 잘못된 정보 시 경고 메시지
- **홈 화면**
    - 다양한 분류별 모임, 인기 모임장, 신규 모임, 마감 임박 모임 등 섹션 제공
    - 각 섹션별로 좌우 스크롤로 모임 확인 가능
    - 최상단 : 인기 모임, 신규 모임, 인기 모임장 프로필, 광고 배너, 마감 임박 모임, 맞춤형 추천 모임
- **모임 상세 페이지**
    - 모임 사진, 이름, 신청 인원, 시간, 위치, 준비물, 비용 등 확인
    - 모임 소개, 프로그램, FAQ, 후기, 모임장 정보 제공
    - 오늘부터 3일 이내 날씨 정보(온도, 체감 온도, 바람, 습도 등)
    - 유사 모임 추천, 찜하기 및 신청 기능
- **모임 신청**
    - 달력에서 날짜·시간 선택, 필수 정보 미입력 시 경고 메시지
    - 신청 완료 시 선택 날짜·시간, 장소, 준비물 확인 가능
- **마이 페이지**
    - 기본 정보, 찜한 모임, 참가한 모임, 작성한 리뷰 등 관리
    - 로그아웃 시 로그인 화면으로 이동
    - 로그인 상태 유지
    - 다른 아이디로 로그인 시 데이터베이스 정보 연동
 
<br>

### 📌 시연 영상

![Image](https://github.com/user-attachments/assets/6e2eb569-2980-4b46-b17d-5901b65f56fe)

<br>

### 📌 기술 스택

#### 개발 언어
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white">

#### 데이터베이스
<img src="https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=SQLite&logoColor=white">


