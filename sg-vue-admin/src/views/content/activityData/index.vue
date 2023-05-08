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
          <el-form-item label="志愿者" prop="summary">
            <el-input
              v-model="queryParams.checked"
              placeholder="请选择"
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
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              @click="handleDelete"
            >删除</el-button>
          </el-col>

          <!-- <right-toolbar
            :show-search.sync="showSearch"
            :columns="columns"
            @queryTable="getList"
          /> -->
        </el-row>

        <el-table :data="articleList" style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="活动ID" align="center" />
          <el-table-column prop="title" label="标题" align="center" />
          <el-table-column prop="userName" label="志愿者" align="center" />
          <el-table-column prop="checkedName" label="审核状态" align="center"/>
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
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="志愿活动" prop="name">
          <el-input v-model="form.name" placeholder="请输入志愿活动" />
        </el-form-item>
        <el-form-item label="用户名" prop="description">
          <el-input v-model="form.description" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-upload
          class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          :on-change="handleChange"
          :file-list="fileList">
        </el-upload>
        <el-form-item label="审核状态">
          <el-select v-model="form.status" placeholder="请选择">
            <el-option :key="'0'" label="通过" :value="'0'" />
            <el-option :key="'1'" label="不通过" :value="'1'" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
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

// import {getCategory} from "@/api/content/category";

export default {
  name: 'ActivityData',
  data() {
    return {

      fileList: [{
        name: 'food.jpeg',
        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
      }, {
        name: 'food2.jpeg',
        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
      }],
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
  watch: {},
  created() {
    this.getList()
  },
  methods: {
    handleChange(file, fileList) {
      this.fileList = fileList.slice(-3);
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        pid: null,
        description: null,
        metaKeywords: null,
        metaDescription: null,
        status: 0,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true
      listActivityData(this.queryParams).then((response) => {
        this.articleList = response.rows
        this.articleList.forEach(item =>{
          if(item.checked == 0){
            item.checkedName = "通过"
          }else if(item.checked == 1){
            item.checkedName = "待审核"
          }else {
            item.checkedName = "驳回"
          }
        })
        console.log(this.articleList)
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
      const id = row.id || this.ids
      getCategory(id).then(response => {
        this.form = response
        this.open = true
        this.title = '审核材料'
      })
      // this.$router.push('/write?id=' + row.id)
    },
    /** 新增用户 */
    handleAdd() {
      this.$router.push('/write')
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除分类编号为"' + ids + '"的数据项？').then(function() {
        return delArticle(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
