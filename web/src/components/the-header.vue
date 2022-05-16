<template>
  <a-layout-header >
    <div class="logo">mywiki</div>

    <a-popconfirm
        cancel-text="否"
        ok-text="是"
        title="确认退出登录?"
        @confirm="logout()"
    >
      <a v-show="user.id" class="login-menu" >
        <span>退出登录</span>
      </a>
    </a-popconfirm>

    <a v-show="user.id" class="login-menu">
      <span>你好，{{ user.name }}</span>
    </a>

    <a v-show="!user.id" class="login-menu" @click="showLoginModal">
      <span>登录</span>
    </a>

    <a-menu
        mode="horizontal"
        theme="dark"
    >
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>

      <a-menu-item key="/about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/category" >分类管理</router-link>
      </a-menu-item>




    </a-menu>


    <a-modal
        title="登录"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login"
    >
      <a-form :label-col="{ span: 6 }" :model="loginUser" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  setup () {
    // 用来登录后保存
    const user=computed(()=> store.state.user)


    // 用来登录
    const loginUser = ref({
      loginName: "test",
      password: "test"
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    // 登录
    const login = () => {
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");
          store.commit("setUser",data.content)
        } else {
          message.error(data.message);
        }
      });
    };
    // 退出登录
    const logout = () => {
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.get('/user/logout/'+ user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("退出登录成功！");
          store.commit("setUser",{})
        } else {
          message.error(data.message);
        }
      });
    };

    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      logout,
      user
    }
  }
});
</script>

<style>
.logo {
  width: 70px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.2);*/
  /*margin: 0 20px 0 0;*/
  float: left;
  color: white;
  font-size: 18px;
}
.login-menu{
  float: right;
  color: white;
  margin-left: 10px;
}

</style>
