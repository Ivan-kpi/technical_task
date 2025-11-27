package technikal.task.fishmarket.controllers;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import technikal.task.fishmarket.models.Fish;
import technikal.task.fishmarket.models.FishDto;
import technikal.task.fishmarket.models.FishImage;
import technikal.task.fishmarket.services.FishRepository;

@Controller
@RequestMapping("/fish")
public class FishController {
	
	@Autowired
	private FishRepository repo;
	
	@GetMapping({"", "/"})
	public String showFishList(Model model) {
		List<Fish> fishlist = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		model.addAttribute("fishlist", fishlist);
		return "index";
	}
	
	@GetMapping("/create")
	public String showCreatePage(Model model) {
		FishDto fishDto = new FishDto();
		model.addAttribute("fishDto", fishDto);
		return "createFish";
	}
	
	@GetMapping("/delete")
	public String deleteFish(@RequestParam int id) {

        try {
            Fish fish = repo.findById(id).orElseThrow();

            for (FishImage img : fish.getImages()) {
                try {
                    Path imagePath = Paths.get("public/images/" + img.getFileName());
                    Files.deleteIfExists(imagePath);
                } catch (Exception ex) {
                    System.out.println("Cannot delete image file: " + ex.getMessage());
                }
            }
            repo.delete(fish);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
		
		return "redirect:/fish";
	}
	
	@PostMapping("/create")
	public String addFish(@Valid @ModelAttribute FishDto fishDto, BindingResult result) {
		
		if(fishDto.getImageFiles().isEmpty()) {
			result.addError(new FieldError("fishDto", "imageFile", "Потрібне фото рибки"));
		}
		
		if(result.hasErrors()) {
			return "createFish";
		}

		Date catchDate = new Date();

        Fish fish = new Fish();
        fish.setCatchDate(catchDate);
        fish.setName(fishDto.getName());
        fish.setPrice(fishDto.getPrice());

		try {
			String uploadDir = "public/images/";
			Path uploadPath = Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

            for (MultipartFile image : fishDto.getImageFiles()) {
                if (image.isEmpty()) {
                    continue;
                }

                String storageFileName = catchDate.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream,
                            uploadPath.resolve(storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                FishImage fishImage = new FishImage();
                fishImage.setFileName(storageFileName);
                fishImage.setFish(fish);
                fish.getImages().add(fishImage);
            }
			
		}catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
		repo.save(fish);
		
		return "redirect:/fish";
	}

}
