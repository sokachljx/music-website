<template>
  <div class="table">
    <div cxlass="container">
      <div class="handle-box">
        <el-button class="handle-del mr10" type="primary" size="mini" @click="delAll">批量删除</el-button>
        <el-input
          v-model="select_word"
          class="handle-input mr10"
          size="mini"
          placeholder="请输入歌单名"
        ></el-input>
        <el-button
          type="primary"
          size="mini"
          @click="centerDialogVisible = true"
          >添加歌单</el-button
        >
      </div>
      <el-table
      

        size="mini"
        border
        style="width:100%"
        height="440px"
        :data="data"
        @selection-change="handleSelectionChange"
      >
      <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column label="歌单图片" width="110" align="center">
          <template slot-scope="scope">
            <div class="songList-img">
              <img :src="getUrl(scope.row.pic)" style="width:100%" />
            </div>
            <el-upload
              
              :action="uploadsongListUrl(scope.row.id)"
              :on-success="handleAvatorSuccess"
              :before-upload="beforeAvatorUpload"
            >
              <el-button size="mini">更新图片</el-button>
            </el-upload>
          </template>
        </el-table-column>
        <el-table-column
          prop="title"
          label="标题"
          width="120"
          align="center"
        ></el-table-column>
       
        <el-table-column label="简介">
          <template slot-scope="scope">
            <p style="height: 100px; overflow: scroll">
              {{ scope.row.introduction }}
            </p>
          </template>
        </el-table-column>
        <el-table-column
          prop="style"
          label="风格"
          width="120"
          align="center"
        ></el-table-column>
        <el-table-column label="歌曲管理" width="110" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="songEdit(scope.row.id)">歌曲管理</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="currentPage"
          :page-size="pageSize"
          :total="tableData.length"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>
    </div>
    <el-dialog
      title="添加歌单"
      :visible.sync="centerDialogVisible"
      width="400px"
      center
    >
      <el-form :model="registerForm" ref="registerForm" label-width="80px">
        <el-form-item prop="title" label="标题" size="mini">
          <el-input v-model="registerForm.title" placeholder="标题">
          </el-input>
        </el-form-item>
        
        <el-form-item prop="introduction" label="简介" size="mini">
          <el-input
            placeholder="简介"
            v-model="registerForm.introduction"
            type="textarea"
          >
          </el-input>
        </el-form-item>
        <el-form-item prop="style" label="风格" size="mini">
          <el-input v-model="registerForm.style" placeholder="风格">
          </el-input>
        </el-form-item>
      </el-form>
      <span class="dialog-footer" slot="footer">
        <el-button size="mini" @click="centerDialogVisible = false"
          >取 消</el-button
        >
        <el-button type="primary" size="mini" @click="addSongList"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <!-- 编辑弹出框 -->
    <el-dialog title="修改歌单" :visible.sync="editVisible" width="400px">
      <el-form ref="form" :model="form" label-width="60px">
        <el-form-item prop="title" label="标题" size="mini">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        
        <el-form-item prop="introduction" label="简介" size="mini">
          <el-input type="textarea" v-model="form.introduction"></el-input>
        </el-form-item>
        <el-form-item prop="style" label="风格" size="mini">
          <el-input v-model="form.style" placeholder="风格">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="editVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="saveEdit"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <!-- 删除提示框 -->
    <el-dialog title="删除歌单" :visible.sync="delVisible" width="300px" center>
      <div class="del-dialog-cnt" align="center">
        删除不可恢复，是否确定删除？
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="delVisible = false">取 消</el-button>
        <el-button type="primary" size="mini" @click="deleteRow"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    
  </div>
</template>

<script>
import { getAllSongList,setSongList,updateSongList,delSongList } from "../api/index";
import { mixin } from "../mixins";
export default {
  mixins: [mixin],
  data() {
    return {
      centerDialogVisible: false,
      editVisible: false, //编辑弹窗是否显示
      delVisible: false, //删除弹窗是否显示
      multipleSelection: [],  //删除多选
      registerForm: {
        //添加框
        title: "",
        introduction: "",
        style:""
      },
      form: {
        // 编辑框信息
        id: "",
        title: "",
        introduction: "",
        style:""
      },
      tableData: [],
      tempDate: [],
      select_word: "",
      pageSize: 5, // 页数
      currentPage: 1, // 当前页
      idx: -1 //当前选择项
    };
  },
  computed: {
    // 计算当前表格中的数据
    data() {
      return this.tableData.slice(
        (this.currentPage - 1) * this.pageSize,
        this.currentPage * this.pageSize
      );
    }
  },
  //搜索框的内容发生变化的时候，搜索结果table列表的内容跟着他的内容发生变化
  watch: {
    select_word: function() {
      if (this.select_word == "") {
        this.tableData = this.tempDate;
      } else {
        this.tableData = [];
        for (let item of this.tempDate) {
          if (item.title.includes(this.select_word)) {
            this.tableData.push(item);
          }
        }
      }
    }
  },
  created() {
    this.getData();
  },
  methods: {
    // 查询所有歌单
    getData() {
      var _this =this;
      this.tableData = [];
      this.tempDate = [];
      getAllSongList().then(res => {
        this.tableData = res;
        this.tempDate = res;
        this.currentPage=1
      });
    },
    
    // 添加歌单
    addSongList() {
     
      let params = new URLSearchParams();
      params.append("title", this.registerForm.title);
      params.append("pic", "/img/songListPic/qi1.png");
      params.append("introduction", this.registerForm.introduction);
      params.append("style", this.registerForm.style);
      setSongList(params)
        .then(res => {
          if (res.code == 1) {
            this.getData();
            this.notify("添加成功", "success");
          } else {
            this.notify("添加失败", "error");
          }
        })
        .catch(err => {
          console.log(err);
        });
      this.centerDialogVisible = false;
    },
    // 更新图片
    uploadsongListUrl(id) {
      return `${this.$store.state.HOST}/songList/updateSongListPic?id=${id}`;
    },
    // 获取当前页
    handleCurrentChange(val) {
      this.currentPage = val;
    },

    // 编辑
    handleEdit(row) {
      this.editVisible = true;
      this.idx = row.id;
      let datetime = row.birth;
      this.form = {
        id: row.id,
        title: row.title,
        introduction: row.introduction,
        style:row.style
      };
    },
    // 保存编辑
    saveEdit() {
      let params = new URLSearchParams();
      params.append("id", this.form.id);
      params.append("title", this.form.title);
      params.append("introduction", this.form.introduction);
      params.append("style", this.form.style);
      updateSongList(params)
        .then(res => {
          if (res.code === 1) {
            this.getData();
            this.notify("修改成功", "success");
          } else {
            this.notify("修改失败", "error");
          }
        })
        .catch(err => {
          console.log(err);
        });
      this.editVisible = false;
    },
    // 确定删除
  deleteRow() {
    delSongList(this.idx)
      .then(res => {
        if (res) {
          this.getData();
          this.notify("删除成功", "success");
        } else {
          this.notify("删除失败", "error");
        }
      })
      .catch(err => {
        console.log(err);
      });
    this.delVisible = false;
  },
  // 转向歌曲管理界面
  songEdit (id) {
      this.$router.push({path: `/ListSong`, query: {id: id}})
    }
  }
   
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}
.songList-img {
  width: 100%;
  height: 80px;
  border-radius: 5px;
  margin-bottom: 5px;
  overflow: hidden;
}
.handle-input {
  width: 300px;
  display: inline-block;
}
.pagination {
  display: flex;
  justify-content: center;
}
</style>
