<template>
<div style="margin: 5px auto; width: 60%">
  <blog-list :title="title"/>
</div>
</template>

<script>
import BlogList from "@/components/BlogList.vue"
export default {
    name:"Search",
    data(){
        return{
            title:this.$route.query.title
        }
    },
    components:{
        BlogList,
    }
}
</script>

<style>

</style>