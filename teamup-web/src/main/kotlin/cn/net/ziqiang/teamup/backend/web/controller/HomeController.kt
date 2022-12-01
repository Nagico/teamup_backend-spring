package cn.net.ziqiang.teamup.backend.web.controller

import cn.net.ziqiang.teamup.backend.common.pojo.vo.team.TeamInfoVO
import cn.net.ziqiang.teamup.backend.service.service.TeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/home")
class HomeController {
    @Autowired
    private lateinit var teamService: TeamService

    @GetMapping("/teams")
    fun getTeams(
        @RequestParam("competition", required = false) competition: String?,
        @RequestParam("role", required = false) role: String?,
        @RequestParam("id", required = false) id: Long?
    ): List<TeamInfoVO> = teamService.searchTeams(competition, role, id)
}