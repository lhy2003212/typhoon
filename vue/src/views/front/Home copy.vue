<template>
  <div class="main-content">
    <div style="display:flex; grid-gap:10px" >
      
      <div style="width:150px" class="card">
        <div class="category-item">全部博客</div>
        <div class="category-item">后端</div>
        <div class="category-item">前端</div>
      </div>
      <div style="flex:1" class="card"></div>
      <div style="width:260px;" class="card"></div>
    </div>

  </div>
</template>

<script>

export default {
  
  data() {
    return {

    }
  },
  mounted() {

  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {

  }
}
</script>

<style>
.category-item{
  text-align: center;
  padding: 10px 0;
  font-size: 16px;
  
}

.category-item-active{
  background-color: #2c334c;
  color: #fff;
  border-radius: 5px;
}

</style>
