# DaumBookSearch
Android application to search for books with Daum API

[APK 링크 Github]
[APK 링크 Google Drive]


## 목차

- [요구사항 정리](#요구사항-정리)
- [개발 환경](#개발-환경)
- [프로젝트 구성](#프로젝트-구성)
    - [1. Directory](#1-Directory)
    - [2. Architecture Design Pattern and Paging](#2-Architecture-Design-Pattern-and-Paging)
    - [3. Dependency Injection](#3-Dependency-Injection)
    - [4. CI](#4-CI)
- [화면 구성](#화면-구성)
    - [1. 스플래시 화면](#1-스플래시-화면)
    - [2. 검색 화면](#2-검색-화면)
    - [3. 상세 화면](#3-상세-화면)
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
    * Dagger 2.27       // Dependency Injection Tool
    * Retrofit 2.8.1    // REST API Tool
    * OkHttp 4.7.0      // HTTP Client Tool
    * Glide 4.11.0      // Image Loading Tool
    * Timber 4.7.1      // Logging Tool
    * Logger 2.2.0      // Logging Tool
    
  * 이미지 출처
    * 앱 아이콘 : 자체 제작
    * 기본 아이콘 : MATERIAL DESIGN
    * 카카오 캐릭터 : 카카오 프렌즈 월페이퍼
    
    
## 프로젝트 수행 전략

### 1. Directory

```
/com/meuus90
│
├── base             ---------> # base package
│   ├── arch/util        ---------> # architecture util source
│   ├── common/util      ---------> # common util source
│   ├── constant         ---------> # constant source
│   └── view             ---------> # custom view source
│
└── daumbooksearch   ---------> # project package
    ├── di               ---------> # dependency injection
    ├── model
    │   ├── data/source
    │   │   ├── api         ----------> # remote server api
    │   │   ├── local       ----------> # local room dao
    │   │   └── repository  ----------> # repository source
    │   ├── paging       ---------> # paging source
    │   └── schema       ---------> # schema collection
    ├── viewmodel    ---------> # viewmodel source
    ├── view         ---------> # view source
    └── DaumBookSearch.kt  ---> # application
```

### 2. Architecture Design Pattern and Paging

  * 아키텍쳐 디자인 패턴은 MVVM 패턴을 적용하였다.
  
    * 각 컴포넌트들은 필요시 다른 컴포넌트를 Inject하여 사용하였다. [Dependency Injection](#3-Dependency-Injection)
    
    * View는 MainActivity, SplashFragment, BookListFragment, BookDetailFragment로 구성하였다.[화면 구성](#화면-구성)
    
        * View 컴포넌트들은 필요 시 ViewModel에게 트리거를 발생시키고 필요한 데이터를 관찰한다.
        
    * ViewModel은 SplashViewModel, BooksViewModel로 구성하였다.
    
        * 각 ViewModel은 필요한 비즈니스 로직을 처리하고 데이터를 저장하거나 변경된 내용을 알린다.
        
        * SplashViewModel은 로컬 캐시 데이터를 초기화하는 기능이 있다.
        
        * BooksViewModel은 BooksRepository로 View와 Model 간 paging 관련 처리를 중계한다.
        
    * Model은 BooksRepository와 Local DB, Newwork API로 구성하였다.
    
        * BooksRepository는 Pager를 이용하여 Remote DataSource에서 필요한 데이터를 Room에 저장한다. 
        
        * Config 파라미터에 따라 PagingData를 메모리에 캐싱하여 관찰자에게 알린다.
        
        
  * BookListFragment의 RecyclerView에 페이징 기능을 적용하였다.
  
    * AndroidX Paging 3.0.0-alpha05 라이브러리를 사용하였다.
    
        * 사전에 Paging 2 버전을 사용하여 구성하였지만 PagedListBoundaryCallback와 Adapter의 비정상 처리 등의 이슈가 발생하여 Paging 3 버전으로 업데이트 하였다.
        
        * Paging 3 이상 버전에서는 PagedList와 PagedListAdapter가 Deprecated 되었고 PagingData와 PagingDataAdapter가 생겼으며 사용방법에 다소 차이점이 있다.
  
    * Paging 처리 방식은 'Network Storage -> Local Storage -> Repository -> Adapter'로 구성하였다.
    
        * BooksRepository에서 제공하는 기본 페이징 처리는 Room 로컬 스토리지에서 캐싱처리 하도록 하였다.
        
        * 로컬 스토리지 데이터가 모두 로드 되었고 추가 데이터가 필요할 시 BooksPageKeyedMediator를 이용하여 네트워크에서 추가 데이터를 수집하여 로컬 스토리지에 저장한다.
        
        * BookListAdapter는 PagingDataAdapter를 상속하고 Diff Callback을 설정하여 아이템이 중복으로 나오는 것을 방지하였다.
        
        * 페이징 처리에 적용한 파라미터는 다음과 같다.
        
    ```
    const val localPagingSize = 25          // Room에서 페이지당 불러오는 아이템 개수
    const val localInitialLoadSize = 40     // PagingData를 초기화할 때 Room에서 불러오는 초기 아이템 개수
    const val localPrefetchDistance = 25    // PagingDataAdapter에서 스크롤 시 
                                            // 아이템을 미리 불러오기 위해 메모리상 남은 개수

    const val remotePagingSize = 50         // Network에 요청할 페이지 당 아이템 개수 
                                            // (PagingConfig에 적용하지 않고 Request 파라미터로 넘긴다.)
    ```
      

### 3. Dependency Injection

#### 각 컴포넌트들을 모듈화 하여 컴포넌트간 종속성을 제거하였다. 
#### 이를 통해 개발 퍼포먼스가 향상되었고 단위 테스트를 수행하기 쉬워졌으며 코드 재사용성이 늘어났다.

  * Fragment를 각각 모듈화 하였고, Activity도 각각 모듈화하여 사용할 Fragment들을 서브모듈로 등록하였다.
```
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(
        modules = [
            SplashFragmentModule::class,
            BookListFragmentModule::class,
            BookDetailFragmentModule::class
        ]
    )
    internal abstract fun contributeMainActivity(): MainActivity
}
```
  
  * AppModule에서는 Application Context, 네트워크 API, Room Database 등을 모듈화하였다.
```
@Provides
@Singleton
fun appContext(application: Application): Context {
    return application
}

@Provides
@Singleton
fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
    val ok = OkHttpClient.Builder()
        .connectTimeout(timeout_connect, TimeUnit.SECONDS)
        .readTimeout(timeout_read, TimeUnit.SECONDS)
        .writeTimeout(timeout_write, TimeUnit.SECONDS)
            
                .
                .
                .
            
    ok.addInterceptor(interceptor)
    return ok.build()
}
```

  * 생성된 컴포넌트 모듈들은 AppComponent에서 바인드하여 AppInjector를 통해 Application에 주입하였다.
```

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        MainActivityModule::class
    ]
)

interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

    fun inject(app: DaumBookSearch)
}
```


### 4. CI

  * Github Actions Workflow를 이용해 테스트 자동화를 등록하였다. [Github Actions](https://github.com/meuus90/DaumBookSearch/actions)
  
  * 주요 기능
  
    * develop branch에서 commit push 완료시 실행
    
    * JDK i.8 테스트 환경 셋업
    
    * Kotlin linter 체크
    
    * Android linter 체크
    
    * Test code Unit test 실시


## 화면 구성

### 1. 스플래시 화면

  * 로컬 캐시 데이터를 초기화한다.
  
  * 500ms 이후 메인 검색 화면으로 이동한다.


### 2. 검색 화면

  * 디바운싱 검색 기능을 제공한다. 입력창에 텍스트를 입력하고 500ms가 지나면 최종 입력된 문자열로 조회한다.
  
  * 리스트가 검색되면 RecyclerView의 Adapter에 submit 한다.
  
  * appbar_scrolling_view_behavior를 적용하였다.
  
  * 리스트를 아래로 스크롤 시 정해진 페이징 처리방식에 따라 아이템을 자동으로 추가한다.
  
  * 스피너를 이용하여 정렬방식이나 검색 필드를 제한하는 기능을 제공한다.
  
  * 좌측 상단 아이콘을 클릭하면 리스트 최상단으로 이동한다.
  
  * 아이템을 클릭하면 상세 화면으로 이동한다. Back 버튼을 클릭하면 앱을 종료한다.
  
  * 상세 화면 이동 시 이미지 transition animation 애니메이션을 적용하였다.


### 3. 상세 화면

  * API 검색으로 받을 수 있는 item 내용을 표현한다.
  
  * 하단 버튼 클릭 시 브라우저를 열어 링크 페이지로 보낸다.
  
  * Back 버튼을 클릭하거나 좌측 상단 버튼 클릭 시 메인 검색 화면으로 이동한다.


## 작업 계획
- [x] 프로젝트 세팅
- [x] 스키마 디자인
- [x] Model 세팅
    - [x] Repository 세팅
    - [x] Room 세팅
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
