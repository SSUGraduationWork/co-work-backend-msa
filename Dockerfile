FROM node

#작업 디렉토리 설정. 이 디렉토리에서 어플리케이션을 실행하고 종속성을 설치한다.
WORKDIR /usr/src/app

#npm install의 정상 수행을 위해, 그 전에 컨테이너 내부에 패키지 모듈 명세 파일 복사 (종속성 파일 복사)
COPY package*.json ./

#도커 서버가 수행할 커맨드 추가 (필요한 종속성 설치)
RUN npm install

#package.json 외에 어플리케이션 소스 코드 복사
COPY . .

#컨테이너가 실행될 포트 설정
EXPOSE 5000

#컨테이너가 실행될 때 1번만 수행되는 {시작 명령어} 자리에 들어갈 커맨드
CMD ["npm", "start"]