<script setup>
import { ref } from 'vue'
import useUserInfoStore from '@/stores/userinfo.js'
const userInfoStore = useUserInfoStore()
const userInfo = ref({
    ...userInfoStore.info
})

//定义数据模型
const passwordData = ref({
    old_pwd: '',
    new_pwd: '',
    re_pwd: ''
})


//校验密码的函数
const checkRePassword = (rule, value, callback)=>{
    if(value === ''){
        callback(new Error('请再次确认密码'))
    }else if(value !== passwordData.value.new_pwd){
        callback(new Error('请确保两次输入的密码一样'))
    }else{
        callback()
    }
}

//密码校验规则
const rules = {
    old_pwd: [
        {required:true, message:'请输入原密码', trigger:'blur'},
    ],
    new_pwd: [
        {required:true, message:'请输入密码', trigger:'blur'},
        {min:5, max:16, message:'长度为5-16位非空字符', trigger:'blur'}
    ],
    re_pwd: [
        {validator:checkRePassword, trigger:'blur'}
    ]
}

//修改个人信息
import { passwordUpdateService } from '@/api/user.js'
import { ElMessage } from 'element-plus'
import { useTokenStore } from '@/stores/token.js'
import { useRouter } from 'vue-router'

const tokenStore = useTokenStore()
const router = useRouter()

const updatePassword = async()=>{
    //调用接口
    let result = await passwordUpdateService(passwordData.value)
    ElMessage.success(result.message ? result.message : '密码修改成功，请重新登录')

    //清除token并跳转到登录页面
    tokenStore.removeToken()
    userInfoStore.removeInfo()
    router.push('/login')
}
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>重置密码</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="passwordData" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="登录名称">
                        <el-input v-model="userInfo.username" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="原密码" prop="old_pwd">
                        <el-input v-model="passwordData.old_pwd" type="password" show-password></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="new_pwd">
                        <el-input v-model="passwordData.new_pwd" type="password" show-password></el-input>
                    </el-form-item>
                    <el-form-item label="确认新密码" prop="re_pwd">
                        <el-input v-model="passwordData.re_pwd" type="password" show-password></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updatePassword">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>