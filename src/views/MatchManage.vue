<template>
  <div>赛事管理页面</div>
</template>

<template>
  <div>
    <!-- 新增赛事按钮 -->
    <el-button type="primary" @click="handleAdd">新增赛事</el-button>

    <!-- 赛事列表表格 -->
    <el-table :data="matches" border style="margin-top: 20px">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="赛事名称"></el-table-column>
      <el-table-column prop="homeTeam" label="主队"></el-table-column>
      <el-table-column prop="awayTeam" label="客队"></el-table-column>
      <el-table-column prop="matchTime" label="比赛时间"></el-table-column>
      <el-table-column prop="venueName" label="场馆"></el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑赛事弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="赛事名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="主队">
          <el-input v-model="form.homeTeam"></el-input>
        </el-form-item>
        <el-form-item label="主队Logo URL">
          <el-input v-model="form.homeLogo" placeholder=""></el-input>
        </el-form-item>
        <el-form-item label="客队">
          <el-input v-model="form.awayTeam"></el-input>
        </el-form-item>
        <el-form-item label="客队Logo URL">
          <el-input v-model="form.awayLogo" placeholder=""></el-input>
        </el-form-item>
        <el-form-item label="比赛时间">
          <el-date-picker
            v-model="form.matchTime"
            type="datetime"
            placeholder="选择比赛时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="开票时间">
          <el-date-picker
            v-model="form.saleTime"
            type="datetime"
            placeholder="选择开票时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="场馆">
          <el-select v-model="form.venueId" placeholder="请选择场馆" @change="onVenueChange">
            <el-option
              v-for="item in venues"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="最低票价">
          <el-input-number v-model="form.minPrice" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="最高票价">
          <el-input-number v-model="form.maxPrice" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="赛事海报URL">
          <el-input v-model="form.poster"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      matches: [],
      venues: [],          // 场馆列表，用于下拉框
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        name: '',
        homeTeam: '',
        awayTeam: '',
        homeLogo: '',
        awayLogo: '',
        matchTime: '',
        saleTime: '',
        venueId: null,
        venueName: '',
        poster: '',
        status: 1,         // 默认状态：售票中
        minPrice: 0,
        maxPrice: 0
      }
    }
  },
  created() {
    this.fetchMatches()
    this.fetchVenues()
  },
  methods: {
    fetchMatches() {
      this.$http.get('/match/list', { params: { page: 1, size: 100 } }).then(res => {
        console.log('原始响应:', res);
      // 后端返回结构为 {code:0, data:{list: [], total: 0}}
        if (res && res.code === 0 && res.data && Array.isArray(res.data.list)) {
          this.matches = res.data.list;
        } else {
          this.matches = [];
        }
        console.log('赛事列表:', this.matches);
      }).catch(err => {
        console.error('获取赛事列表失败', err);
        this.matches = [];
      });
    },
    fetchVenues() {
      this.$http.get('/venue/list').then(res => {   // res 是 {code, message, data}
        if (res && res.code === 0 && Array.isArray(res.data)) {
          this.venues = res.data;
        } else {
          this.venues = [];
          console.warn('场馆数据格式异常', res);
        }
        console.log('场馆列表', this.venues);
      }).catch(err => {
      console.error('获取场馆列表失败', err);
      this.venues = [];
    });
  },
    onVenueChange(venueId) {
      const venue = this.venues.find(v => v.id === venueId)
      if (venue) {
        this.form.venueName = venue.name
      }
    },
    // 打开新增弹窗
    handleAdd() {
      this.dialogTitle = '新增赛事'
      this.form = {
        id: null,
        name: '',
        homeTeam: '',
        awayTeam: '',
        homeLogo: '',
        awayLogo: '',
        matchTime: '',
        saleTime: '',
        venueId: null,
        venueName: '',
        poster: '',
        status: 1,
        minPrice: 0,
        maxPrice: 0
      }
      this.dialogVisible = true
    },
    // 编辑（暂留，可后续完善）
    handleEdit(row) {
      this.dialogTitle = '编辑赛事'
      this.form = { ...row }
      this.dialogVisible = true
    },
    // 提交表单
    submitForm() {
      if (this.form.id) {
        // 编辑
        this.$http.put('/match/update', this.form).then(() => {
          this.$message.success('更新成功')
          this.dialogVisible = false
          this.fetchMatches()
        })
      } else {
        // 新增
        this.$http.post('/match/add', this.form).then(() => {
          this.$message.success('添加成功')
          this.dialogVisible = false
          this.fetchMatches()
        })
      }
    },
    // 删除赛事
    handleDelete(id) {
      this.$confirm('确定删除该赛事吗？', '提示', { type: 'warning' }).then(() => {
        this.$http.delete(`/match/delete/${id}`).then(() => {
          this.$message.success('删除成功')
          this.fetchMatches()
        })
      })
    }
  }
}
</script>