<template>
  <a-layout>
    <a-layout-content style="background: #fff;padding: 24px;margin: 0;min-height: 200px">
      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="level1.length>0"
              :tree-data="level1"
              :defaultExpandAll="true"
              :replace-fields="{title: 'name',key: 'id',children: 'children'}"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">

        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

<script>
import {defineComponent, onMounted, ref} from "vue";
import {message} from "ant-design-vue";
import axios from "axios";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
export default defineComponent({
  name: 'doc',
  setup(){
    const level1 = ref();
    level1.value = [];
    const route=new useRoute()
    const handleQuery = () => {
      axios.get("/doc/all/"+route.query.ebookId).then((response) => {
        const data = response.data;
        if(data.success){
          level1.value = Tool.array2Tree(data.content,0);
          console.log(level1.value);
        }else{
          message.error(data.message);
        }
      })
    }
    onMounted(()=>{
      handleQuery();
    })
    return{
      level1,
    }
  }
})
</script>