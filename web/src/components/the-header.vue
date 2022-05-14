<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu
        theme="dark"
        :style="{ lineHeight: '64px' }"
        mode="horizontal"
    >
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook">
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>
      <a class="login-menu" @click="showLoginModal">
        <span>登录</span>
      </a>
    </a-menu>

    <a-modal
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        title="登录"
        @ok="login"
    >
      <a-form :label-col="{ span: 6 }" :model="user" :wrapper-col="{ span: 18 }">
        <a-form-item label="登陆名">
          <a-input v-model:value="user.loginName" />
        </a-form-item>

        <a-form-item label="密码" >
          <a-input v-model:value="user.password"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>


<script lang="ts">
import {defineComponent, ref} from "vue";
export default defineComponent({
  name: "the-header",
  setup(){
    // -------- 表单 ---------
    const user = ref({
      loginName:"test",
      password:"test"
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal=()=>{
      loginModalVisible.value = true;
      // user.value = Tool.copy(record)
    }
    const login=()=>{
      console.log("开始登录")
    }
    // const handleLoginModalOk = () => {
    //   loginModalLoading.value = true;
    //
    //   axios.post("/user/login", user.value).then((response) => {
    //     loginModalLoading.value = false;
    //     const data = response.data;
    //     if (data.success) {
    //       loginModalVisible.value = false;
    //     } else {
    //       message.error(data.message)
    //     }
    //   });
    // };
    return{
      loginModalVisible,
      loginModalLoading,
      user,
      login,
      showLoginModal
    }
  }
})
</script>

<style>
.login-menu {
  color: white;
  margin-left: 800px;
  order: 999;
}
</style>