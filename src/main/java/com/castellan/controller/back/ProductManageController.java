package com.castellan.controller.back;

import com.castellan.common.Const;
import com.castellan.common.ResponseCode;
import com.castellan.common.ServerResponse;
import com.castellan.pojo.Product;
import com.castellan.pojo.User;
import com.castellan.service.IFileService;
import com.castellan.service.IProductService;
import com.castellan.service.IUserService;
import com.castellan.util.PropertiesUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/back/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse saveProduct(HttpSession session, Product product) {
        return iProductService.saveOrUpdateProduct(product);
    }


    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, int productId, int status) {
        return iProductService.setSaleStatus(productId, status);
    }


    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, int productId) {
        return iProductService.getDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iProductService.getProductList(pageNum, pageSize);

    }


    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse searchProduct(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, String productName, Integer productId) {
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);

    }


    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();

        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }

        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;

    }


}
