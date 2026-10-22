import { createRouter, createWebHistory } from "vue-router";

// 페이지 import
import MainPage from "../pages/home/MainPage.vue";
import DefaultLayout from "@/pages/layout/DefaultLayout.vue";
import LoginPage from "@/pages/login/LoginPage.vue";
import BoardWrite from "@/pages/board/write/BoardWrite.vue";
import BoardDetail from "@/pages/board/detail/BoardDetail.vue";
import PlanList from "@/pages/travel/planList/PlanList.vue";
import TravelPlan from "@/pages/travel/plan/TravelPlan.vue";
import MyPage from "@/pages/myPage/MyPage.vue";

const routes = [
	{
		path: "/",
		component: DefaultLayout,
		// 헤더가 필요한 페이지
		children: [
			{
				path: "",
				name: "home",
				component: MainPage,
			},
			{
				path: "/board/write",
				name: "boardWrite",
				meta: { requiresAuth: true }, // 로그인 필요
				component: BoardWrite,
			},
			{
				path: "/board/detail/:id",
				name: "boardDetail",
				component: BoardDetail,
			},
			{
				path: "/travel/plan/list",
				name: "planList",
				meta: { requiresAuth: true }, // 로그인 필요
				component: PlanList,
			},
			{
				path: "/travel/plan/:id",
				name: "travelPlan",
				meta: { requiresAuth: true }, // 로그인 필요
				component: TravelPlan,
			},
			{
				path: "/my-page/:userId",
				name: "myPage",
				component: MyPage,
			},
		],
	},
	{
		path: "/login",
		name: "login",
		component: LoginPage,
	},
];

const router = createRouter({
	history: createWebHistory(),
	routes,
});

// 로그인 필요 페이지 접근 시 처리
router.beforeEach((to, from, next) => {
	if (to.meta.requiresAuth && !sessionStorage.getItem("userId")) {
		next("/");
	} else {
		next();
	}
});

export default router;
