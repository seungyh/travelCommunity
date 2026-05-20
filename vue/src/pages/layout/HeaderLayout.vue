<template>
    <div class="header-area">
        <div class="logo-area">
            <span class="title">여행 커뮤니티</span>
        </div>
        <div class="header-menu-area absolute left-3/7">
            <div class="mr-[5px]!"><span class="btn w-[60px]  text-center p-[2px]" :class="[
                route.path === '/'
                    ? 'bg-teal-500 text-white'
                    : 'hover:bg-gray-100'
            ]"><font-awesome-icon :icon="['far', 'house']" /> 홈</span></div>
            <div class="mr-[5px]!"><span class="btn w-[80px] hover:bg-gray-100 text-center p-[2px]" :class="[
                route.path === '/post'
                    ? 'bg-teal-500 text-white'
                    : 'hover:bg-gray-100'
            ]"><font-awesome-icon
                        :icon="['far', 'newspaper']" />게시글</span></div>
            <div><span class="btn w-[80px] hover:bg-gray-100 text-center p-[2px]"><font-awesome-icon
                        :icon="['far', 'compass']" />여행
                    <font-awesome-icon icon="chevron-down"/>
                    </span></div>
        </div>
        <div class="header-menu-right-area absolute right-1/20">
            <div class="btn hover:bg-gray-100 w-[30px] text-center content-center mr-[10px]!">
                <font-awesome-icon icon="magnifying-glass"/>
            </div>
            <div class="mr-[10px]!">
                <router-link v-if="isLogin" to="/"> <span class="btn text-center p-[2px] bg-teal-500 w-[100px] hover:bg-teal-600 text-white"><font-awesome-icon icon="pencil" class="mr-[5px]!"/>글쓰기</span></router-link>
                <router-link v-else to="/login"> <span class="btn text-center p-[2px] bg-teal-500 w-[100px] hover:bg-teal-600 text-white"><font-awesome-icon icon="arrow-right-to-bracket" class="mr-[5px]!"/>로그인</span></router-link>
            </div>
            <div class="btn hover:bg-gray-100 w-[30px] text-center content-center">
                <font-awesome-icon
                        :icon="['far', 'bell']" />
            </div>
        </div> 
    </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import { useRoute } from 'vue-router'

const route = useRoute()
const isLogin = ref(false);
</script>