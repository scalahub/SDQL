package sdql

import dql.DQLUtil
import javax.servlet.http.HttpServlet
//import jdql.util.DQLUtil
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.sh.reflect.DefaultTypeHandler
import org.sh.reflect.EasyProxy
import org.sh.easyweb.Text

class InitializeProxyServlet extends HttpServlet {
  InitializeProxy // refer to initialize object to start EasyProxy server
}

class AnalyzerBackendServlet extends HttpServlet {
  override def doPost(req:HttpServletRequest, resp:HttpServletResponse) = doGet(req, resp)
  override def doGet(req:HttpServletRequest, resp:HttpServletResponse) = {  
    val code = req.getParameter("code");
    if (code == null) throw new Exception("No parameter 'code' found")
    val json = AnalyzerBackend.doAnalysis(code)
    resp.getWriter.print(json)
  }
}

class AnalyzerSettingsGetServlet extends HttpServlet {
  override def doPost(req:HttpServletRequest, resp:HttpServletResponse) = doGet(req, resp)
  override def doGet(req:HttpServletRequest, resp:HttpServletResponse) = {  
    val json = AnalyzerBackend.getBugPatterns
    resp.getWriter.print(json)
  }
}

class AnalyzerSettingsSetServlet extends HttpServlet {
  override def doPost(req:HttpServletRequest, resp:HttpServletResponse) = doGet(req, resp)
  override def doGet(req:HttpServletRequest, resp:HttpServletResponse) = {  
    val setting = req.getParameter("setting");
    if (setting == null) throw new Exception("No parameter 'setting' found")
    val json = AnalyzerBackend.setActiveBugPatterns(setting)
    resp.getWriter.print(json)
  }
}

class UserCreateServlet extends HttpServlet {
  override def doPost(req:HttpServletRequest, resp:HttpServletResponse) = doGet(req, resp)
  override def doGet(req:HttpServletRequest, resp:HttpServletResponse) = {  
    val userID = req.getParameter("userID");
    if (userID == null) throw new Exception("No parameter 'userID' found")
    val json = AnalyzerMultiTenant.userCreate(userID)
    resp.getWriter.print(json)
  }
}

class UserDoAnalysisServlet extends HttpServlet {
  override def doPost(req:HttpServletRequest, resp:HttpServletResponse) = doGet(req, resp)
  override def doGet(req:HttpServletRequest, resp:HttpServletResponse) = {  
    val userID = req.getParameter("userID");
    if (userID == null) throw new Exception("No parameter 'userID' found")
    val code = req.getParameter("code");
    if (code == null) throw new Exception("No parameter 'code' found")
    val json = AnalyzerMultiTenant.userDoAnalysis(userID, new Text(code))
    resp.getWriter.print(json)
  }
}

class UserGetBugPatterns extends HttpServlet {
  override def doPost(req:HttpServletRequest, resp:HttpServletResponse) = doGet(req, resp)
  override def doGet(req:HttpServletRequest, resp:HttpServletResponse) = {  
    val userID = req.getParameter("userID");
    if (userID == null) throw new Exception("No parameter 'userID' found")
    val json = AnalyzerMultiTenant.userGetBugPatterns(userID)
    resp.getWriter.print(json)
  }
}

class UserDeleteBugPattern extends HttpServlet {
  override def doPost(req:HttpServletRequest, resp:HttpServletResponse) = doGet(req, resp)
  override def doGet(req:HttpServletRequest, resp:HttpServletResponse) = {  
    val userID = req.getParameter("userID");
    if (userID == null) throw new Exception("No parameter 'userID' found")
    val patternID = req.getParameter("patternID");
    if (patternID == null) throw new Exception("No parameter 'patternID' found")
    val json = AnalyzerMultiTenant.userDeleteBugPattern(userID, patternID)
    resp.getWriter.print(json)
  }
}

object InitializeProxy {
  DQLUtil
  Config // ???
  Compiler // load basis
  GenerateWebBoxHTML.formObjects.foreach{obj =>
    EasyProxy.addProcessor("", obj, DefaultTypeHandler, true)
  }
  println("INITIALIZE DONE")
//    EasyProxy.addProcessor("", Analyzer, DefaultTypeHandler, true)
//    EasyProxy.addProcessor("", SDQLAdmin, DefaultTypeHandler, true)
//    EasyProxy.addProcessor("", SDQLCompiler, DefaultTypeHandler, true)
//    EasyProxy.addProcessor("", SolidityCompiler, DefaultTypeHandler, true)
}








