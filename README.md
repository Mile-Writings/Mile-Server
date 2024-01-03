## 📖 MILE (Make It Look Easy)

> 오직, 글모임을 위한 글쓰기 플랫폼

 
 <br/>



## 💗 Our Team

|도소현|박희정|
|:------:|:------:|
|<img src="https://avatars.githubusercontent.com/u/79795051?v=4" width="300" height="300" />|<img src="https://github.com/GOSOPT-CDS-TEAM2/frontend/assets/97084864/c6d35974-8fc1-4089-93be-9430d4d33d2d" width="300" height="300" />|
|[@sohyundoh](https://github.com/sohyundoh)|[@parkheeddong](https://github.com/parkheeddong)|


<br/>

## 🌳 Branch Convention
<details>
	<summary> 브랜치 전략 </summary>
  	<div markdown="1">
      <img src="https://github.com/Mile-Writings/Mile-Server/assets/116873401/013c85c1-675e-484a-a453-83de917f5684" width = "500" >
  	</div>
</details>

- `main` : 프로덕션용 브랜치 (배포 시 사용될 버전만 존재)
- `develop` : 개발 전용 브랜치
- `이슈 기반 브랜치`
    - 생성된 이슈 번호로 브랜치를 생성한다
    - feat/#{이슈번호}
    - ex. feat/#1,
    - ex. hotfix/#3
- PR이 Merge되면 해당 브랜치는 삭제한다.



<br />


## 📌 Commit Convention
**이슈번호 [tag] 커밋 내용 요약**

ex. #123 [feat] 로그인 구현

| tag | description |
| --- | --- |
| feat | 새로운 기능 추가 |
| fix | 자잘한 수정 |
| bugfix | 버그 수정 |
| refactor | 코드 리팩토링 시에만 사용 |
| chore | config 및 라이브러리, 빌드 관련 파일 수정 (프로덕션 코드 수정 x) |
| rename | 파일명, 변수명 수정 |
| docs | 문서 수정 |
| comment | 주석 추가 및 수정 |
| remove | 기능 삭제 및 파일 삭제 |
| test | 테스트 코드 작성 |
| hotfix | hotfix |

<br />


## 📍 Architecture
<img src="https://github.com/Mile-Writings/Mile-Server/assets/116873401/f3eaa175-19fc-4ceb-8ab4-49687ddebf0e" width = "900" >

## 👨🏻‍💻 Stack
| Stack | Version | Content |
| --- | --- | --- |
| Spring Boot | 3.2.1 | - |
| JDK | 17 | - |
| MySql | 8.0.33 | - |
| Openfeign - Spring Cloud | 2022.0.4 | 소셜 로그인 연동 |
| Redis | 2.3.1 | 리프레시 토큰 저장 |
| QueryDsl | 5.0.0 | 동적 쿼리 생성 |

<br />

## 🗂️ Directory
```jsx
├── build.gradle
├── 🗂️ module-api
│   ├── build.gradle
│   └── 📂 src/main/java/com/mile
│       ├── 📂 common
│       ├── 📂 config
│       └── 📂 post # 도메인 별로 controller 분리
├── 🗂️ module-auth
│   ├── build.gradle
│   └── 📂 src/main/java/com/mile
│       └── 📂 external
│           └── 📂 client
├── 🗂️ module-common
│   ├── build.gradle
│   └── 📂 src/main/java/com/mile
│        └── 📂 exception
│            ├── 📂 message
│            └── 📂 model
│        └── 📂 util
├── 🗂️ module-domain
│   ├── build.gradle
│   └── 📂 src/main/java/com/mile
│          └── 📂 post #도메인 별로 분리
│              ├── 📂 domain
│              ├── 📂 repository
│              └── 📂 service
└── settings.gradle
```
