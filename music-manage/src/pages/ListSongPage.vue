<template>
  <div class="table">
    <div class="crumbs">
      <i class="el-icon-tickets">歌单歌曲信息</i>
    </div>
    <div cxlass="container">
      <div class="handle-box">
        <el-button
          class="handle-del mr10"
          type="primary"
          size="mini"
          @click="delAll"
          >批量删除</el-button
        >
        <el-input
          v-model="select_word"
          class="handle-input mr10"
          size="mini"
          placeholder="请输入歌曲名"
        ></el-input>
        <el-button
          type="primary"
          size="mini"
          @click="centerDialogVisible = true"
          >添加歌曲</el-button
        >
      </div>
      <el-table
        size="mini"
        border
        style="width:100%"
        height="440px"
        :data="tableData"
        @selection-change="handleSelectionChange"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="40"></el-table-column>
        
        <el-table-column
          prop="name"
          label="歌手-歌名"
          
          align="center"
        ></el-table-column>

        
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
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
      title="添加歌曲"
      :visible.sync="centerDialogVisible"
      width="400px"
      center
    >
      <el-form
        :model="registerForm"
        ref="registerForm"
        label-width="80px"
        action=""
        id="tf"
      >
        <el-form-item prop="singerName" label="歌手名字" size="mini">
          <el-input v-model="registerForm.singerName" aria-placeholder="歌手名字"></el-input>
        </el-form-item>
        <el-form-item prop="songName" label="歌曲名字" size="mini">
          <el-input v-model="registerForm.songName" aria-placeholder="歌曲名字"></el-input>
        </el-form-item>
      </el-form>
      <span class="dialog-footer" slot="footer">
        <el-button size="mini" @click="centerDialogVisible = false"
          >取 消</el-button
        >
        <el-button type="primary" size="mini" @click="getSongId">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 删除提示框 -->
    <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
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

import { mixin } from "../mixins";
import {listSongDetail, songOfSongId,songOfSongName,delListSong,listSongAdd} from "../api/index";
export default {
  mixins: [mixin],
  data() {
    return {
      centerDialogVisible: false,
      delVisible: false, //删除弹窗是否显示
      multipleSelection: [], //删除多选
      registerForm: {
        //添加框
       singerName:'',
       songName:''
      },
      tableData: [],
      tempDate: [],
      select_word: "",
      songListId:'',
      pageSize: 5, // 页数
      currentPage: 1, // 当前页
      idx: -1 //当前选择项
     
    };
  },
  
  //搜索框的内容发生变化的时候，搜索结果table列表的内容跟着他的内容发生变化
  watch: {
    select_word: function() {
      if (this.select_word == "") {
        this.tableData = this.tempDate;
      } else {
        this.tableData = [];
        for (let item of this.tempDate) {
          if (item.name.includes(this.select_word)) {
            this.tableData.push(item);
          }
        }
      }
    }
  },
  created() {
    this.songListId = this.$route.query.id;
    this.getData();
  },
  
  methods: {
    // 查询所有歌曲
    getData() {
      this.tableData = [];
      this.tempDate = [];
      listSongDetail(this.songListId).then((res) => {
        for(let item of res){
          this.getSong(item.songId);
        }
      })
    },
    //根据歌曲id查询歌曲对象，放到tempData和tableData里面
    getSong(id){
      songOfSongId(id)
      .then(res=>{
        // this.tempData.push(res);
        this.tableData.push(res);
        // console.log(this.tempDate)
      })
      .catch(err =>{
        console.log(err);
      });
    },
    //添加歌曲前的准备，获取到歌曲id
    getSongId(){
      let _this=this;
      var songOfName= _this.registerForm.singerName+"-"+_this.registerForm.songName;
      songOfSongName(songOfName).then(
        res=>{
          _this.addSong(res[0].id)
        }
      )
    },
    // 添加歌曲
    addSong(songId) {
      let _this=this;
      let params=new URLSearchParams();
      params.append("songId",songId);
      params.append("songListId",this.songListId);
      listSongAdd(params)
      .then(res => {
        if(res.code == 1){
          this.getData();
          this.notify("添加成功","success");
        }else{
          this.notify("添加失败","error");
        }
      })
      .catch(err => {
        console.log(err);
      })
      
      _this.centerDialogVisible = false;
    },
    
    // 删除一条歌曲
    deleteRow() {
      delListSong(this.idx,this.songListId)
      .then(res=>{
        if(res){
          this.getData();
          this.notify("删除成功","success");
        }else{
          this.notify("删除失败","error");
        }
      })
      .catch(err=>{
        console.log(err);
      });
      this.delVisible = false;
    },
    // 获取当前页
    handleCurrentChange(val) {
      this.currentPage = val;
    },
  }

}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}
.song-img {
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
.play {
    position: absolute;
    z-index: 100;
    width: 80px;
    height: 80px;
    top: 18px;
    left: 15px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
}
.icon {
  width: 2em;
  height: 2em;
  color: white;
  fill: currentColor;
  overflow: hidden;
}
</style>
