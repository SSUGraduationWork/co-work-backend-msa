# Co-Work (MSA)
>학생들의 팀프로젝트 참여도 향상 및 기여도 시각화 제공을 위한 k8s 클라우드 환경에서의 협업 관리 마이크로서비스
<img width="1000" alt="대표 화면" src="https://github.com/SSUGraduationWork/msa/assets/72440759/9df5471b-c45e-4a59-a688-4b26f37665e8">

## 프로젝트 소개
대학교 팀 프로젝트에서는 학점에 대한 중요도나 프로젝트 결과물에 대한 기대치가 개인별로 다르기 때문에 학생들의 참여도와 노력의 정도가 다른 경우가 많습니다. 하지만 일반적으로 팀 프로젝트에서 평가자인 교수님은 각 팀의 팀 프로젝트가 제대로 이루어졌는지, 팀원들의 기여도가 어떻게 되는지 등을 알 수 없고 최종 결과물만 보고 평가할 수밖에 없기 때문에 공정한 평가가 불가능한 환경이라고 생각했습니다. 이에 저희는 팀 프로젝트 평가 방식의 한계를 극복하고, 팀 프로젝트를 최대한 공정하고 효과적으로 진행할 수 있게 도와주는 ‘Co-Work’ 웹 서비스를 개발하게 되었습니다. ‘Co-Work’는 학생들의 팀 프로젝트 과정을 투명하게 공개하고, 개인별 기여도를 자동으로 측정해주기 때문에 동료 평가 방식에 비해 신뢰도가 높습니다. 저희의 웹 서비스를 이용한다면, 팀 프로젝트에서 발생하는 기여도 불균형에 따른 학생들의 손해를 최소화하고 학생들의 참여도를 향상시킬 수 있을 것입니다. 또한 교수님께서 팀 프로젝트를 평가하실 때 저희의 웹이 좋은 지표가 될 것입니다.

### 주요 기능
1️⃣ 자동 기여도 측정 및 팀원 전체 기여도 그래프 제공

2️⃣ 팀 프로젝트를 효과적으로 진행할 수 있는 워크스페이스

3️⃣ 사용자가 진행하는 프로젝트 전체 관리


## 개발 환경 구성도
<img width="700" alt="개발 환경 구성도" src="https://github.com/SSUGraduationWork/msa/assets/72440759/9dd64d49-6475-4077-87f1-831db099f0fb">

## 아키텍처
### Service
`config-service`
`discovery-service`
`apigateway-service`
`board-service`
`calendar-service`
`user-service`
`dashboard-service`
`work-service`
`chat-service`

<img width="700" alt="개발 환경 구성도" src="https://github.com/SSUGraduationWork/msa/assets/72440759/fff07cdc-29c5-4f14-978b-6415349717cf">


### 서비스 화면
|대시보드|작업|파일|
|----|----|----|
|<img width="1470" alt="대시보드1" src="https://github.com/SSUGraduationWork/msa/assets/72440759/d52ecacd-b841-4b88-a6ee-bd054689b4c7">|<img width="1470" alt="작업" src="https://github.com/SSUGraduationWork/msa/assets/72440759/2cc3d3ae-4301-47d9-a708-57a97aab08f6">|<img width="1470" alt="파일" src="https://github.com/SSUGraduationWork/msa/assets/72440759/48405dc9-dda9-4223-9206-7fc253c73b58">|

|캘린더|채팅|기여도|
|----|----|----|
|<img width="1470" alt="캘린더" src="https://github.com/SSUGraduationWork/collaborativemanagement/assets/125520029/67706a37-d8db-4478-aab0-4c9e91b2ca97">|<img width="1470" alt="채팅" src="https://github.com/SSUGraduationWork/msa/assets/72440759/2ae0e8a3-aa9e-4d3c-8dc7-b47764c8f6c3">|<img width="1470" alt="기여도" src="https://github.com/SSUGraduationWork/msa/assets/72440759/7a88efc8-ca6d-442a-91af-0db8b4678ded">|


