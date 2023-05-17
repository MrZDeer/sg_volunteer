<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <el-form
          v-show="showSearch"
          ref="queryForm"
          :model="queryParams"
          :inline="true"
          label-width="68px"
        >
          <el-form-item label="活动" prop="title">
            <el-input
              v-model="queryParams.title"
              placeholder="请输入活动标题"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
            >搜索</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
<!--          <el-col :span="1.5">-->
<!--            <el-button-->
<!--              type="primary"-->
<!--              plain-->
<!--              icon="el-icon-plus"-->
<!--              size="mini"-->
<!--              @click="handleAdd"-->
<!--            >新增</el-button>-->
<!--          </el-col>-->
<!--          <el-col :span="1.5">-->
<!--            <el-button-->
<!--              type="danger"-->
<!--              plain-->
<!--              icon="el-icon-delete"-->
<!--              size="mini"-->
<!--              @click="handleDelete"-->
<!--            >删除</el-button>-->
<!--          </el-col>-->

          <!-- <right-toolbar
            :show-search.sync="showSearch"
            :columns="columns"
            @queryTable="getList"
          /> -->
        </el-row>

        <el-table :data="articleList" style="width: 100%" @selection-change="handleSelectionChange">
<!--          <el-table-column type="selection" width="55" />-->
          <el-table-column prop="articleId" label="活动ID" align="center" />
          <el-table-column prop="title" label="活动标题" align="center" />
          <el-table-column prop="userName" label="志愿者" align="center" />
          <el-table-column prop="createTime" label="申报时间" align="center" :formatter="formatterTime"/>
          <el-table-column prop="checkedName" label="审核状态" align="center">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status == 0" type="warning">待审核</el-tag>
              <el-tag v-if="scope.row.status == 1" type="success">通过</el-tag>
              <el-tag v-if="scope.row.status == 2" type="danger">驳回</el-tag>
            </template>
          </el-table-column>

          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >审核</el-button>
<!--              <el-button-->
<!--                size="mini"-->
<!--                type="text"-->
<!--                icon="el-icon-delete"-->
<!--                @click="handleDelete(scope.row)"-->
<!--              >删除</el-button>-->
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <el-pagination
      :page-size.sync="queryParams.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :page-sizes="[10, 20, 30, 40]"
      :current-page.sync="queryParams.pageNum"
      @current-change="getList"
      @size-change="getList"
    />



    <!-- 添加或修改分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="志愿活动" prop="name">
          <el-input v-model="form.title" placeholder="请输入志愿活动" disabled/>
        </el-form-item>
        <el-form-item label="姓名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入内容" disabled/>
        </el-form-item>
        <el-form-item label="学号" prop="sno">
          <el-input v-model="form.sno" placeholder="请输入内容" disabled/>
        </el-form-item>
        <el-form-item label="手机号" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="请输入内容" disabled/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入内容" disabled/>
        </el-form-item>

        <el-form-item label="审核状态">
          <el-select v-model="form.status" placeholder="请选择">
            <el-option :key="'0'" label="待审核" :value="'0'" />
            <el-option :key="'1'" label="通过" :value="'1'" />
            <el-option :key="'2'" label="驳回" :value="'2'" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleChange">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

// import { getToken } from '@/utils/auth'
import {
  listArticle,
  delArticle, updateArticle
} from '@/api/content/article'
import {listActivityData} from "@/api/content/activityData";
import {getCategory} from "@/api/content/category";
import {listDeclaration, updateDeclaration} from "@/api/content/declaration";
import {getUser} from "@/api/system/user";

// import {getCategory} from "@/api/content/category";

export default {
  name: 'ActivityData',
  data() {
    return {

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined
      },
      title: '',
      // 是否显示弹出层
      open: false,
      // 总条数
      total: 0,
      articleList: [],
      showSearch: true,
      form:{},

    }
  },
  watch: {
    '$route' (to, from) {
      this.$router.go(0);
    }
  },
  created() {
    this.getList()
  },
  methods: {
    formatterTime(row,column){
      let data = row[column.property]
      return  /\d{4}-\d{1,2}-\d{1,2}/g.exec(data)
    },

    // 表单重置
    reset() {
      this.form = {
        userId: null,
        userName:null,
        phonenumber:null,
        email:null,
        sno:null,
        articleId: null,
        createTime: null,
        status: null,
        title: null,
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true
      listDeclaration(this.queryParams).then((response) => {
        this.articleList = response.rows
        this.articleList.forEach(item =>{
          if(item.status == 0){
            item.checkedName = "待审核"
          }else if(item.status == 1){
            item.checkedName = "通过"
          }else if(item.status == 2){
            item.checkedName = "驳回"
          }else {
            item.checkedName = "系统未知错误"
          }
        })
        this.total = response.total
        this.loading = false
      })
    },
    handleStatusChange(row){
      console.log(row)
      this.$modal.confirm("确认执行该操作？").then(function() {
        return updateArticle(row)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('修改成功')
      }).catch(() => {})
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const userId = row.userId
      this.form.id = row.id
      getUser(userId).then(response => {
        this.form.userId = response.user.id
        this.form.userName = response.user.userName
        this.form.phonenumber = response.user.phonenumber
        this.form.email = response.user.email
        this.form.sno = response.user.sno
        this.form.title = row.title
        this.form.articleId = row.articleId
        this.form.createTime = row.createTime
        this.form.status = row.status
        this.open = true
        this.title = '申报审核'
      })
    },
    //确认审核
    handleChange() {
      let data = {
        id:this.form.id,
        articleId:this.form.articleId,
        userId:this.form.userId,
        status:this.form.status
      }
      updateDeclaration(data)
      this.$modal.msgSuccess('更新成功')
      this.$router.go(0);
    }

  }
}
</script>
