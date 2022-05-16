<template>
  <a-layout>
    <a-layout-sider style="background: #fff" width="200">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
            <MailOutlined />
            <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
            <span><user-outlined />{{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div v-show="isShowWelcome" class="welcome">
        <the-welcome />
      </div>
      <a-list v-show="!isShowWelcome" :data-source="ebooks" :grid="{ gutter: 20, column: 3 }" item-layout="vertical" size="large">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span>
                <component v-bind:is="'FileOutlined'" style="margin-right: 8px" />
                {{ item.docCount }}
              </span>
              <span>
                <component v-bind:is="'UserOutlined'" style="margin-right: 8px" />
                {{ item.viewCount }}
              </span>
              <span>
                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px" />
                {{ item.voteCount }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId='+item.id">{{ item.name }}</router-link>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import {Tool} from "@/util/tool";
import TheWelcome from '@/components/the-welcome.vue';


export default defineComponent({
  name: 'Home',
  components:{TheWelcome},
  setup() {
    const ebooks = ref();
    let categoryId2=0
    const level1 =  ref();
    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;


          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);

        } else {
          message.error(data.message);
        }
      });
    };
    const handleQueryEbook=()=>{
      axios.get("/ebook/list", {
        params: {
          page: 1,
          size: 1000,
          categoryId2: categoryId2
        }
      }).then((response) => {
        const data = response.data;
        ebooks.value = data.content.list;
      });
    }
    const isShowWelcome=ref(true)

    const handleClick = (value:any) => {
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      } else {
        categoryId2=value.key
        isShowWelcome.value = false;
        handleQueryEbook()

      }
    };


    onMounted(() => {
      handleQueryCategory();
      // handleQueryEbook()
    });

    return {
      isShowWelcome,
      ebooks,
      pagination: {
        onChange: (page: any) => {
          console.log(page)
        },
        pageSize: 3,
      },

      handleClick,
      level1,
    }
  }
});
</script>

