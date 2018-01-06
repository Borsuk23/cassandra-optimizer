package eu.asyroka.msc.config;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.jobexecutor.FailedJobCommandFactory;
import org.springframework.stereotype.Service;

@Service
public class FailedJobCommandFactoryImpl implements FailedJobCommandFactory {
	@Override
	public Command<Object> getCommand(String s, Throwable throwable) {
		return new Command<Object>() {
			@Override
			public Object execute(CommandContext commandContext) {
				return null;
			}
		};
	}
}
