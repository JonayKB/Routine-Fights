package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.iespuertodelacruz.routinefights.activity.domain.Activity;
import es.iespuertodelacruz.routinefights.activity.domain.ports.primary.IActivityService;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityInputV2;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityOutputV2;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.mappers.ActivityOutputV2Mapper;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

@Controller
@CrossOrigin
public class ActivityControllerV2 {
    private IActivityService activityService;
    private ActivityOutputV2Mapper activityOutputV2Mapper;
    private IUserService userService;

    public ActivityControllerV2(IActivityService activityService, ActivityOutputV2Mapper activityOutputV2Mapper,
            IUserService userService) {
        this.activityService = activityService;
        this.activityOutputV2Mapper = activityOutputV2Mapper;
        this.userService = userService;
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("createActivity")
    public ActivityOutputV2 createActivity(@Argument ActivityInputV2 activityInput) {

        User user = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Activity activity = activityService.save(activityInput.name(), activityInput.description(),
                activityInput.image(),
                activityInput.timeRate(), activityInput.timesRequiered(), user.getId(), activityInput.categoryID());

        return activityOutputV2Mapper.toDTO(activity);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("paginationActivitiesV2")
    public List<ActivityOutputV2> paginationActivities(@Argument int page, @Argument int perPage) {
        List<Activity> activities = activityService.getPagination(page, perPage);
        return activityOutputV2Mapper.toDTO(activities);
    }

}
