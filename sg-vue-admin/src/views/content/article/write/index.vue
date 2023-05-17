<template>
  <div class="app-container">

    <el-form :rules="rules" ref="form" :model="form" label-width="90px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="活动标题" prop="title">
            <el-input
              v-model="form.title"
              placeholder="请输入活动标题"
              maxlength="30"
              required="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="分类"  prop="category">
            <el-select v-model="form.categoryId" placeholder="请选择">
              <el-option
                v-for="category in categoryList"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
              <!-- <el-option :key="'1'" label="女" :value="'1'" /> -->
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="所需人数" prop="needNumber">
            <el-input
              v-model="form.needNumber"
              placeholder="请输入所需人数"
              type='number'
              maxlength="1000"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="开始/结束时间" prop="time">
            <el-date-picker
              v-model="rangeDate"
              type="datetimerange"
              range-separator="至"
              value-format="yyyy-MM-dd HH:mm:ss"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="活动摘要" prop="summary">
            <el-input v-model="form.summary" type="textarea" />
          </el-form-item>
        </el-col>
         <el-col :span="6">
          <el-form-item label="允许评论">
            <el-radio-group v-model="form.isComment">
              <el-radio :key="'0'" :label="'0'">正常</el-radio>
              <el-radio :key="'1'" :label="'1'">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="是否置顶">
            <el-radio-group v-model="form.isTop">
              <el-radio :key="'0'" :label="'0'">是</el-radio>
              <el-radio :key="'1'" :label="'1'">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>

      </el-row>


      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="缩略图">
            <el-upload
              :file-list="fileList"
              class="upload-demo"
              list-type="picture"
              drag
              name="img"
              action="upload"
              :on-remove="fileRemove"
              :limit="1"
              :http-request="handleUpload"
              :on-exceed="onExceed"
            >
              <i class="el-icon-upload" />
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-show="activeIndex">
          <el-form-item label="成员">
            <!--            <el-select v-model="form.users" placeholder="请选择" multiple>-->
            <!--              <el-option-->
            <!--                v-for="user in userList"-->
            <!--                :key="user.id"-->
            <!--                :label="user.name"-->
            <!--                :value="user.id"-->
            <!--              />-->
            <!--            </el-select>-->
            <el-table
              :data="(form.users).slice((currentPage-1)*pageSize,currentPage*pageSize)"
              border
              style="width: 100%">
              <el-table-column
                prop="name"
                label="姓名"
                width="180">
              </el-table-column>
              <el-table-column
                prop="sno"
                label="学号"
                width="180">
              </el-table-column>
            </el-table>
            <el-pagination
              layout="total, sizes, prev, pager, next, jumper"
              :total="form.users.length"
              :current-page="currentPage"
              :page-size="pageSize"
              :page-sizes="[5,10]"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            />
          </el-form-item>
        </el-col>

      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item>
            <el-button type="primary" size="medium" @click="handleSubmit">{{ aId?"更新":"发布" }}</el-button>
            <el-button v-if="!aId" type="info" @click="handleSave">暂存</el-button>
            <el-button type="primary" size="medium" @click="handleCancel">{{ "取消" }}</el-button>

          </el-form-item>

        </el-col>
      </el-row>
      <el-row>
        <mavon-editor ref="md" v-model="form.content" @imgAdd="addImg" />
      </el-row>
    </el-form>
  </div>
</template>

<script>
import { listAllCategory } from '@/api/content/category'
import { uploadImg } from '@/api/content/upload'
import { addArticle, getArticle, updateArticle } from '@/api/content/article'
// import { listAllTag } from '@/api/content/tag'
export default {
  name: 'Write',
  data() {
    return {
      rangeDate:[],
      form: {
        title: '',
        thumbnail: '',
        isTop: '1',
        isComment: '0',
        content: '',
        users:[],
        startTime:null,
        endTime:null,
        needNumber:null
      },
      categoryList: [],
      userList: [],
      aId: -1,
      fileList: [],
      pageSize:5,
      currentPage:1,
      activeIndex:false,
      rules: {
        title: [{required: true, message: '请输入标题', trigger: 'blur'}],
        category: [{required: true, message: '请选择分类', trigger: 'blur'}],
        needNumber: [{required: true, message: '请输入所需人数', trigger: 'blur'}],
        time: [{required: true, message: '请选择日期', trigger: 'blur'}],
        summary: [{required: true, message: '请输入摘要', trigger: 'blur'}]
      }

    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.aId = route.query && route.query.id
      },
      immediate: true
    },
    rangeDate: function (val, oldVal) {
      if (val !== null) {
        this.form.startTime = val[0]
        this.form.endTime = val[1]
      } else {
        this.form.startTime = null
        this.form.endTime = null
      }
    }
  },
  created() {
    this.getCategory()
    if (this.aId) {
      this.activeIndex = true
      this.getArticle()
    }
  },
  methods: {
    getArticle() {
      getArticle(this.aId).then(response => {
        console.log(response)
        this.$set(this.rangeDate,0,new Date(response.startTime));
        this.$set(this.rangeDate,1,new Date(response.endTime));
        this.form = response
        this.fileList.push({ name: '缩略图', url: response.thumbnail })
      })
    },
    //处理成员分页
    handleSizeChange(val){
      this.pageSize = val;
    },
    handleCurrentChange(val){
      this.currentPage = val;
    },
    handleSave() {
      this.form.status = '1'
      addArticle(this.form).then(response => {
        this.$modal.msgSuccess('保存草稿成功')
      })
    },
    handleSubmit() {
      if (!this.aId) {
        this.form.status = '0'
        console.log(this.form)
        addArticle(this.form).then(response => {
          this.$modal.msgSuccess('发布成功')
          this.$router.push({ path: '/content/article' })
        })
      } else {
        // 更新博客信息
        updateArticle(this.form).then(response => {
          this.$modal.msgSuccess('更新成功')
          this.$router.push({ path: '/content/article' })
        })
      }

    },
    handleCancel(){
      this.$router.push({ path: '/content/article' })
    },
    onExceed() {
      this.$message.error('只能上传一个图片')
    },
    handleUpload(img) {
      uploadImg(img.file).then(response => {
        this.form.thumbnail = response
        this.fileList.push({ name: img.file.name, url: response })
      }).catch(error => {
        this.$message.error(error.msg)
      })
    },
    fileRemove(file, fileList) {
      this.fileList.pop()
    },
    // 绑定@imgAdd event
    addImg(pos, file) {
      // 第一步.将图片上传到服务器.
      uploadImg(file).then(response => {
        this.$refs.md.$img2Url(pos, response)
      }).catch(error => {
        this.$message.error(error.msg)
      })
    },
    getCategory() {
      listAllCategory().then((response) => {
        this.categoryList = response
      })
      // listAllTag().then(response => {
      //   this.userList = response
      // })
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }
  }
}
</script>
<style scoped>
div .upload-demo {
    /* padding-left: 80px; */
}
.el-col .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;

    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
