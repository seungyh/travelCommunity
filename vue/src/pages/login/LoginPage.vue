<template>
    <div class="w-full h-[100vh] flex flex-nowrap">
        <div class="login-left-area w-[50%] relative border-right">
            <div class="absolute bottom-[70px] w-[600px] left-[50px]">
                    <span class="text-5xl font-bold">
                            "세상은 한 권의 책이다.
                            여행하지 않는 사람은
                            한 페이지만 읽은 것이다."
                    </span>
                    <span>
                        — 성 아우구스티누스
                    </span>
            </div>
        </div>
        <div class="login-right-area w-[50%]">
            <div class="h-[500px] w-[30%] m-auto">
                <div class="rounded-ml bg-gray-400">
                    <span>로그인</span>
                    <span>로그아웃</span>
                </div>
                <div>
                    여행을 시작해볼까요?
                </div>
                <div>
                    <div>
                        이름
                    </div>
                    <div>
                        <input v-model="loginInfo.id" />
                    </div>
                    <div>
                        비밀번호
                    </div>
                    <div>
                        <input v-model="loginInfo.password" />
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>
<script setup lang="ts">
import axios from 'axios';
import { ref } from 'vue';

const loginInfo = ref({
    id: "",
    password: "",
});
const pwCheck = ref("");


const login = () => {
    axios.post("/web/api/account/login", loginInfo.value)
        .then(res => {
            console.log('res', res);
        })
        .catch(error => {
            console.log("error", error);
        })
}
</script>
<style scoped>
/* .login-left-area {

} */

 .m-auto {
    margin: auto !important;
 }
</style>