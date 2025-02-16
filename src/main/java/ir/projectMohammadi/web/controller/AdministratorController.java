package ir.projectMohammadi.web.controller;

import ir.projectMohammadi.model.administor.Administrator;
import ir.projectMohammadi.service.administrator.IAdministratorService;
import ir.projectMohammadi.util.mapper.ModelMapper;
import ir.projectMohammadi.web.viewModel.administrator.AdministratorViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Administrator")
public class AdministratorController {

    @Autowired
    private IAdministratorService administratorService;


    @PostMapping("/saveAdministrator")
    @ResponseBody
    public Boolean saveAdministrator(@RequestBody AdministratorViewModel administratorViewModel) {
        return administratorService.saveAndUpdateAdministrator(ModelMapper.map(administratorViewModel, Administrator.class));
    }

    @GetMapping("/getAllAdministrator")
    @ResponseBody
    public List<AdministratorViewModel> getAllAdministrator() {
        return ModelMapper.mapList(administratorService.getAllAdministrator(), AdministratorViewModel.class);
    }

    @GetMapping("/getAdministrator/{id}")
    @ResponseBody
    public AdministratorViewModel getAdministrator(@PathVariable Long id) {
        return ModelMapper.map(administratorService.getAdministrator(id), AdministratorViewModel.class);
    }

    @DeleteMapping("/deleteAdministratorById/{id}")
    @ResponseBody
    public Boolean deleteAdministratorById(@PathVariable Long id) {
        return administratorService.deleteAdministrator(id);
    }

}
