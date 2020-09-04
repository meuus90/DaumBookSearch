# DaumBookSearch
Android application to search for books with Daum API

[APK 링크 Github]
[APK 링크 Google Drive]


## 목차
- [요구사항 정리](#요구사항-정리)
- [개발 환경](#개발-환경)
- [프로젝트 구성](#프로젝트-구성)
    - [Directory](#Directory)
    - [Continuous Integration](#Continuous-Integration)
    - [Architecture Design Pattern](#Architecture-Design-Pattern)
    - [Dependency Injection](#Dependency-Injection)
    - [Paging Data](#Paging-Data)
- [화면 구성](#화면-구성)
    - [스플래시 화면](#스플래시-화면)
    - [검색 화면](#검색-화면)
    - [상세 화면](#상세-화면)
- [작업 계획](#작업-계획)
- [License](#license)
    
    
## 요구사항 정리
  * Kotlin을 이용하여 개발
  * 카카오 도서 검색 API를 이용하여 도서 검색
    * https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-book
    * size param : 50
  * 스크롤 시 연속 Paging 기능 제공
  * 검색 리스트 결과(메인화면) 및 상세화면으로 구성
  * 메인 리스트와 상세화면은 Fragment로 구성
  * 메인 리스트에서 특정 Item 클릭 시 상세화면으로 이동
  

## 개발 환경
  * 기본 환경
    * OS : Mac OS X
    * IDE : Android Studio 4.0.1
    * JVM target : Java-1.8
    * Kotlin 1.3.72
  
  * AndroidX 라이브러리
    * Core 1.3.1
    * Coroutine 1.3.8
    * MultiDex 2.0.1
    * Lifecycle 1.3.8
    * Room 2.3.0-alpha01
    * Paging 3.0.0-alpha05

  * 기타 라이브러리
    * Dagger 2.27
    * Retrofit 2.8.1
    * OkHttp 4.7.0
    * Glide 4.11.0
    * Timber 4.7.1
    * Logger 2.2.0
    
  * 이미지 출처
    * 앱 아이콘 : 자체 제작
    * 기본 아이콘 : MATERIAL DESIGN
    * 카카오 캐릭터 : 카카오 프렌즈 월페이퍼
    
    
## 프로젝트 구성

### Directory

```
/com/meuus90
│
├── base             ---------> # base package
│   ├── arch/util    ---------> # architecture util source
│   ├── common/util  ---------> # common util source
│   ├── constant     ---------> # constant source
│   └── view         ---------> # custom view source
│
└── daumbooksearch   ---------> # project package
    ├── di           ---------> # dependency injection
    ├── model
    │   ├── data/source
    │   │   ├── local   ------> # local room repository
    │   │   └── remote      
    │   │       ├── api         # server api interface
    │   │       └── repository  # remote repository
    │   ├── paging   ---------> # paging source
    │   └── schema   ---------> # schema collection
    ├── viewmodel    ---------> # viewmodel source
    ├── view         ---------> # view source
    └── DaumBookSearch.kt  ---> # application
```
    
### Continuous Integration
[Github Actions](/actions)
Github Actions Workflow로 테스트 자동화를 등록하였다.
    
    
### Architecture Design Pattern
MVVM
    
    
### Dependency Injection
Dagger


### Paging Data


## 화면 구성

### 스플래시 화면


### 검색 화면


### 상세 화면


## 작업 계획
- [x] 프로젝트 세팅
- [x] 스키마 디자인
- [x] Model 세팅
    - [x] Remote Repository 세팅
    - [x] Local Room 세팅
    - [x] Paging Data 세팅
- [x] ViewModel 세팅
- [x] Unit Test 테스트코드 작성
- [x] UI 디자인
- [x] API 에러 타입 별 대응
- [x] 애니메이션 등 UX 설정
- [x] 디바이스 퍼포먼스 체크
- [x] UI 테스트 및 기타 버그 픽스
- [x] Release


## License

Completely free (MIT)! See [LICENSE.md](LICENSE.md) for more.





