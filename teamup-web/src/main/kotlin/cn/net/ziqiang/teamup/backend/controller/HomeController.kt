package cn.net.ziqiang.teamup.backend.controller

import cn.net.ziqiang.teamup.backend.pojo.pagination.PagedList
import cn.net.ziqiang.teamup.backend.pojo.entity.Team
import cn.net.ziqiang.teamup.backend.util.handleSort
import cn.net.ziqiang.teamup.backend.service.TeamService
import cn.net.ziqiang.teamup.backend.util.security.SecurityContextUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
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
        @RequestParam("search", required = false) search: String?,
        @RequestParam(defaultValue = "-id") order: String,
        @RequestParam("page", required = false, defaultValue = "1") page: Int,
        @RequestParam("size", required = false, defaultValue = "10") size: Int
    ): PagedList<Team> =
        teamService.searchTeams(competition, role, search, PageRequest.of(page - 1, size, handleSort(order)), SecurityContextUtils.userIdOrNull)

    @GetMapping("/teams/rebuild")
    fun rebuildTeams() {
        teamService.rebuildTeamDoc()
    }
}