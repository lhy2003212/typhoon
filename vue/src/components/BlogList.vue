<template>
<div class="card">
    <div class="blog-box" v-for="item in tableData" :key="item.id" v-if="total > 0" >
    <div style="flex: 1; width: 0">
        <div style="blog-title" @click="$router.push('/front/blogDetail?blogId='+item.id)">{{ item.title }}</div>
        <div class="line1" style="color: #666; margin-bottom: 10px; font-size: 13px">{{ item.descr }}</div>
        <div style="display: flex">
        <div style="flex: 1; font-size: 13px">
            <span style="color: #666; margin-right: 20px"><i class="el-icon-user"></i> {{ item.userName }}</span>
            <span style="color: #666; margin-right: 20px"><i class="el-icon-eye"></i> {{ item.readCount }}</span>
            <span style="color: #666"><i class="el-icon-like"></i> {{ item.likesCount }}</span>


            <span v-if="showOpt" style="margin-left: 40px; color: red; cursor: pointer" @click="del(item.id)"><i class="el-icon-delete"></i>删除</span>
            <span v-if="showOpt" style="margin-left: 10px; color: #2a60c9; cursor: pointer" @click="editBlog(item.id)"><i class="el-icon-edit"></i>编辑</span>
        </div>
        <div style="width: fit-content">
            <el-tag v-for="i in JSON.parse(item.tags || '[]')" :key="i" type="primary" style="margin-right:5px">{{ i }}</el-tag>
            <!-- <el-tag type="primary" style="margin-right: 10px">后端</el-tag>
            <el-tag type="primary">面试</el-tag> -->
        </div>
        </div>
    </div>
    <div style="width: 150px">
        <img style="width: 100%; height: 80px; border-radius: 5px" :src="item.cover" alt="">
    </div>
    </div>
    <div v-if="total === 0" style="padding: 20px 0; text-align: center; font-size: 16px; color: #666">暂无数据</div>
    <div style="margin-top: 10px" v-if="total">
    <el-pagination
        background
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20]"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total">
    </el-pagination>
    </div>
</div>
  
</template>

<script>
export default {
    name:"BlogList",
    data(){
        return{
            tableData: [],  // 所有的数据
            pageNum: 1,   // 当前的页码
            pageSize: 10,  // 每页显示的个数
            total: 0,
          }
    },
    props:{
        categoryName:null,
        // title:null,
        type:null,
        showOpt: false,
    },

    //监听数据变化，加载最新数据
    watch:{
        categoryName(){
            this.loadBlogs(1)
        }
    },
    mounted(){
        this.loadBlogs(1)
    },
    methods:{
        editBlog(blogId) {
            window.open('/front/newBlog?blogId=' + blogId)
        },
        del(id) {   // 单个删除
        this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
            this.$request.delete('/blog/delete/' + id).then(res => {
                if (res.code === '200') {   // 表示操作成功
                    this.$message.success('操作成功')
                    this.loadBlogs(1)
                } else {
                    this.$message.error(res.msg)  // 弹出错误的信息
                }
            })
        }).catch(() => {
        })
        },
        loadBlogs(pageNum) {
            if (pageNum) this.pageNum = pageNum
            let url
            switch (this.type) {
                case "user":url="/blog/selectByUserId";break;
                case 'like': url = '/blog/selectLike'; break;
                case 'collect': url = '/blog/selectCollect'; break;
                case 'comment': url = '/blog/selectComment'; break;
                default :url="/blog/selectPage"
            }
            this.$request.get(url, {
                params: {
                pageNum: this.pageNum,
                pageSize: this.pageSize,
                categoryName: this.categoryName ==="全部博客" ? null :this.categoryName,
                title:this.title
                }
            }).then(res => {
                this.tableData = res.data?.list
                this.total = res.data?.total
            })
        },
        handleCurrentChange(pageNum) {
            this.loadBlogs(pageNum)
        },
    }
}
</script>

<style>
.blog-box {
  display: flex;
  grid-gap: 15px;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}
.blog-box:first-child {
  padding-top: 0;
}
.blog-title{
  font-size: 16px; font-weight: bold; margin-bottom: 10px;
  cursor: pointer;
}
.blog-title:hover{
  color: #2a60c9;
}
</style>