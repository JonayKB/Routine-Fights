package es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.controllers;

import java.util.List;

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
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.dtos.ActivityOutputV2Streak;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.mappers.ActivityOutputV2Mapper;
import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.primary.v2.mappers.ActivityOutputV2StreakMapper;
import es.iespuertodelacruz.routinefights.user.domain.User;
import es.iespuertodelacruz.routinefights.user.domain.ports.primary.IUserService;

@Controller
@CrossOrigin
public class ActivityControllerV2 {
    private IActivityService activityService;
    private ActivityOutputV2Mapper activityOutputV2Mapper;
    private ActivityOutputV2StreakMapper activityOutputV2StreakMapper;
    private IUserService userService;

    public ActivityControllerV2(IActivityService activityService, ActivityOutputV2Mapper activityOutputV2Mapper,
            IUserService userService, ActivityOutputV2StreakMapper activityOutputV2StreakMapper) {
        this.activityService = activityService;
        this.activityOutputV2Mapper = activityOutputV2Mapper;
        this.userService = userService;
        this.activityOutputV2StreakMapper = activityOutputV2StreakMapper;
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @MutationMapping("createActivity")
    public ActivityOutputV2 createActivity(@Argument ActivityInputV2 activityInput) {

        User user = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());

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

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("getSubscribedActivities")
    public List<ActivityOutputV2> getSubscribedActivities() {
        User user = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Activity> activities = activityService.getSubscribedActivities(user.getId());
        return activityOutputV2Mapper.toDTO(activities);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("getSubscribedActivitiesWithStreaks")
    public List<ActivityOutputV2Streak> getSubscribedActivitiesWithStreaks() {
        User user = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Activity> activities = activityService.getSubscribedActivitiesWithStreak(user.getId());
        return activityOutputV2StreakMapper.toDTO(activities);
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("paginationActivitiesNotSubscribed")
    public List<ActivityOutputV2> paginationActivitiesNotSubscribed(@Argument int page, @Argument int perPage) {
        User user = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Activity> activities = activityService.getPaginationNotSubscribed(page, perPage, user.getId());
        return activityOutputV2Mapper.toDTO(activities);
    }
    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("getSubscribedActivitiesByName")
    public List<ActivityOutputV2> getSubscribedActivities(@Argument String activityName) {
        User user = userService.findByEmailOnlyBase(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Activity> activities = activityService.getSubscribedActivities(user.getId(), activityName);
        return activityOutputV2Mapper.toDTO(activities);
    }

}
