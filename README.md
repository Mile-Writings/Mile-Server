## 📖 MILE (Make It Look Easy)
<img width="1920" alt="마일" src="https://github.com/user-attachments/assets/4dd983b9-5018-494d-a5ea-a6d4f6533bdb">

> 오직, 글모임을 위한 글쓰기 플랫폼

 <br/>

## 💗 Our Team
|도소현|박희정|
|:------:|:------:|
|[@sohyundoh](https://github.com/sohyundoh)|[@parkheeddong](https://github.com/parkheeddong)|


<br/>

## 📍 Architecture
![image](https://github.com/Mile-Writings/Mile-Server/assets/79795051/3fadfcd5-ae4b-420a-bc5b-313aca7b5b11)

## 🤍 ERD
<img width="718" alt="image" src="https://github.com/Mile-Writings/Mile-Server/assets/79795051/ce6b8805-0547-46f0-a571-563165ddd372">

## 🗂️ Directory
```jsx
├── build.gradle
├── 🗂️ module-api # 프레젠테이션
│   ├── build.gradle
│   └── 📂 src
│       ├──main/java/com/mile
│     	   ├── 📂 common
│          ├── 📂 config
│          └── 📂 controller
│       └── test/java/com/mile
├── 🗂️ module-auth
│   ├── build.gradle
│   └── 📂 src/main/java/com/mile
│           ├── 📂 client
│           ├── 📂 jwt # 레디스 리프레시 토큰 저장용
│           └── 📂 strategy # 로그인 전략패턴
├── 🗂️ module-common
│   ├── build.gradle
│   └── 📂 src/main/java/com/mile
│        ├── 📂 exception
│        ├── 📂 dto
│        ├── 📂 handler
│        ├── 📂 log
│        └── 📂 swagger
├── 🗂️ module-domain
│   ├── build.gradle
│   └── 📂 src/main/java/com/mile
│          └── 📂 post # 도메인 별로 분리
└── 🗂️ module-external # AWS S3
```

