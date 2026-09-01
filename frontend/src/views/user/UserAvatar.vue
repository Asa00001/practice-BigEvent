<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import { ref } from 'vue'
import avatar from '@/assets/default.png'
const uploadRef = ref()
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore()

import useUserInfoStore from '@/stores/userinfo.js'
const userInfoStore = useUserInfoStore()
//用户头像地址
const imgUrl = ref(userInfoStore.removeInfo.userPic)

//图片上传成功的回调函数
const uploadSuccess = (result) => {
    if (!result.data) {
        ElMessage.error('图片上传失败，请重试')
        return
    }

    imgUrl.value = result.data
}

//图片上传失败的提示
const uploadError = () => {
    ElMessage.error('图片上传失败，请重试')
}

//头像修改
import { userAvatarUpdateService } from '@/api/user.js'
import { ElMessage } from 'element-plus'
const updateAvatar = async () => {
    //调用接口
    let result = await userAvatarUpdateService(imgUrl.value)
    ElMessage.success(result.message ? result.message : '修改成功')
    //修改pinia中的数据
    userInfoStore.info.userPic = imgUrl.value
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>更换头像</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-upload ref="uploadRef" class="avatar-uploader" :show-file-list="false" :auto-upload="true"
                    action="/api/upload" name="file" :headers="{ 'Authorization': tokenStore.token }"
                    :on-success="uploadSuccess" :on-error="uploadError">
                    <img v-if="imgUrl" :src="imgUrl" class="avatar" />
                    <img v-else:src="avatar" width="278" />
                </el-upload>
                <p class="upload-tip">
                    支持 JPG、PNG 格式，图片大小建议不超过 1MB。
                </p>
                <br />
                <el-button type="primary" :icon="Plus" size="large"
                    @click="uploadRef.$el.querySelector('input').click()">
                    选择图片
                </el-button>
                <el-button type="success" :icon="Upload" size="large" @click="updateAvatar">
                    上传头像
                </el-button>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
    :deep() {
        .avatar {
            width: 278px;
            height: 278px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 278px;
            height: 278px;
            text-align: center;
        }
    }
}

.upload-tip {
    margin-top: 8px;
    font-size: 12px;
    color: #909399;
}
</style>