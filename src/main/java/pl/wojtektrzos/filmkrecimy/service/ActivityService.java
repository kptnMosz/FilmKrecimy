package pl.wojtektrzos.filmkrecimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.dto.ActivityDto;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.entity.PlanItemRole;
import pl.wojtektrzos.filmkrecimy.repository.ActivityRepository;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemRepository;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemRoleRepository;
import pl.wojtektrzos.filmkrecimy.util.EnterLog;

import java.util.HashSet;
import java.util.Set;

@Service
public class ActivityService {
    @Autowired
    ActivityRepository activityeRepository;
    @Autowired
    PlanItemRoleRepository planItemRoleRepository;
    @Autowired
    PlanItemRepository planItemRepository;

    public void registerNewActivity(ActivityDto activityDto) {
        activityDto.getActivity().setMovie(activityDto.getMovie());
        activityeRepository.save(activityDto.getActivity());
        EnterLog.log("Service", "ActivityDto", activityDto.getPlanItemRole().toString());

        Set<PlanItem> events = new HashSet<>();
        for (int i = 1; i <= activityDto.getNumberOfDays(); i++) {
            PlanItem event = new PlanItem();
            event.setName(activityDto.getActivity().getName() + " " + "day-" + i);
            EnterLog.log("Service", "jaka rola", activityDto.getPlanItemRole().getName() + "");
            event.setPlanItemRoles(activityDto.getPlanItemRole());
            event.setActivity(activityDto.getActivity());
            planItemRepository.save(event);
            EnterLog.log("Service", "ActivityDto", activityDto.getPlanItemRole().toString());
        }
        activityDto.getActivity().setEvents(events);
        activityeRepository.save(activityDto.getActivity());

    }

}
